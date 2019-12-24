package com.esys.framework.uaa.security;

import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.exceptions.UserGroupException;
import com.esys.framework.uaa.service.IUserGroupService;
import com.esys.framework.uaa.service.IUserService;
import com.esys.framework.uaa.web.error.MustChangePasswordException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;


public class UserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    @Autowired
    private ModelMapper modelMapper;

    private PasswordEncoder passwordEncoder;
    private IUserService userService;
    private IUserGroupService userGroupService;
    private String userNotFoundEncodedPassword;

    @Autowired
    public UserDetailsAuthenticationProvider(PasswordEncoder passwordEncoder, IUserService userService
            , IUserGroupService userGroupService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.userGroupService = userGroupService;
    }


    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {

        if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided");
            throw new BadCredentialsException(
                    messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }

        String presentedPassword = authentication.getCredentials()
                .toString();
        User user =(User) userDetails;
        if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            userService.updateLoginAttemptTryCount(user.getId());
            if(user.getLoginAttemptTryCount() >= 10 && user.isEnabled()){
                userService.updateEnable(user.getId(),false);
            }
            logger.debug("Authentication failed: password does not match stored value");
            throw new BadCredentialsException(
                    messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }else{
            if(!userGroupService.findAllByUser(user.getId()).stream().allMatch(l -> l.isEnabled())){
                throw new UserGroupException("uaa.usergroup.notactive");
            }
            LocalDateTime changedDate = user.getLastPasswordModifiedDate();
            if(changedDate == null){
                return;
            }
            LocalDateTime now = LocalDateTime.now();
            changedDate.plusMonths(3);
            if(changedDate.isBefore(now)){
                userService.updateLastLoginTime(user.getId());
            }else{
                throw new MustChangePasswordException();
            }
        }
    }

    @Override
    protected void doAfterPropertiesSet() throws Exception {
        Assert.notNull(this.userService, "A UserDetailsService must be set");
        this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        LinkedHashMap<String,String> map = (LinkedHashMap<String, String>) authentication.getDetails();
        AuthenticationToken auth = new AuthenticationToken(
                authentication.getPrincipal(),
                authentication.getCredentials(),
                map.get("domain"));

        UserDetails loadedUser;

        try {
            loadedUser = this.userService.loadUserByUsernameAndDomain(auth.getPrincipal()
                    .toString(), auth.getDomain());
        } catch (UsernameNotFoundException notFound) {
            if (authentication.getCredentials() != null) {
                String presentedPassword = authentication.getCredentials()
                        .toString();
                passwordEncoder.matches(presentedPassword, userNotFoundEncodedPassword);
            }
            throw notFound;
        } catch (Exception repositoryProblem) {
            throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
        }

        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException("UserDetailsService returned null, "
                    + "which is an interface contract violation");
        }
        return loadedUser;
    }

}
