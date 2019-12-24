package com.esys.framework.core.security;

import com.esys.framework.core.configuration.EsysProperties;
import com.esys.framework.core.consts.AuthoritiesConstants;
import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.dto.uaa.BasicUserWithAuthorityDto;
import com.esys.framework.core.entity.uaa.Authority;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.repository.IUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SecurityUtils {

    @Autowired
    private transient static IUserRepository userRepository;

    @Autowired
    private transient static EsysProperties esysProperties;

    private transient static ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    public static String getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String userName = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                userName = springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                userName = (String) authentication.getPrincipal();
            } else if (authentication.getPrincipal() instanceof BasicUserDto) {
                userName = ((BasicUserDto) authentication.getPrincipal()).getEmail();
            }
        }
        return userName;
    }

    public static BasicUserDto getCurrentBasicUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            if (authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
                OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
                Jwt jwt= JwtHelper.decode(details.getTokenValue());
                if(jwt == null) return null;
                try {
                    return objectMapper.readValue(jwt.getClaims(),BasicUserDto.class);
                } catch (IOException e) {
                    log.error("Claim to BasicUserDto error Claim is : {}" ,jwt.getClaims());
                }
            }else if(authentication.getPrincipal() instanceof BasicUserDto){
                return (BasicUserDto) authentication.getPrincipal();
            }
        }
        return null;
    }

    public static BasicUserDto getCurrentBasicUserFromJwt(String jwtText) {
        BasicUserDto user = null;
                try {
                    Jwt jwt= JwtHelper.decode(jwtText);
                    user = objectMapper.readValue(jwt.getClaims(),BasicUserDto.class);
                } catch (IOException e) {
                    log.error("Claim to BasicUserDto error Claim is : {}" ,jwtText);
                }
        return user;
    }

    public static BasicUserDto loginWithJwt(String jwtText) {
        BasicUserDto user = null;
        SignatureVerifier signatureVerifier = null;
        if(esysProperties != null){
            signatureVerifier = new MacSigner(esysProperties.getSecurity()
                    .getAuthentication().getJwt().getSecret());

        }
        Jwt jwt;
        try {
            try {
                if(signatureVerifier == null){
                    jwt= JwtHelper.decode(jwtText);
                }else {
                    jwt= JwtHelper.decodeAndVerify(jwtText,signatureVerifier);
                }

            }catch (Exception ex){
                throw new InvalidTokenException(jwtText);
            }
            user = objectMapper.readValue(jwt.getClaims(),BasicUserDto.class);
            List<GrantedAuthority> authorities = new ArrayList<>();

            SecurityUser securityUser = objectMapper.readValue(jwt.getClaims(),SecurityUser.class);
            securityUser.getAuthorities().stream().forEach(auth -> authorities.add(new Authority(auth)));
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (IOException e) {
            log.error("Claim to BasicUserDto error Claim is : {}" ,jwtText);
        }
        return user;
    }

    public static Authentication loginWithJwt(String jwtText,String secret) {

        SignatureVerifier signatureVerifier = new MacSigner(secret);
        Jwt jwt;
        try {
            jwt= JwtHelper.decodeAndVerify(jwtText,signatureVerifier);
        }catch (Exception ex){
            throw new InvalidTokenException(jwtText);
        }

        try {
            BasicUserDto user = objectMapper.readValue(jwt.getClaims(),BasicUserDto.class);
            List<GrantedAuthority> authorities = new ArrayList<>();

            SecurityUser securityUser = objectMapper.readValue(jwt.getClaims(),SecurityUser.class);
            securityUser.getAuthorities().stream().forEach(auth -> authorities.add(new Authority(auth)));
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User getCurrentUser() {
        return userRepository.getUser(getCurrentUserLogin(),"esys");
    }

    public static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Collection<? extends GrantedAuthority> authorities = securityContext.getAuthentication().getAuthorities();
        if (authorities != null) {
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(AuthoritiesConstants.ANONYMOUS)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isCurrentUserInRole(String authority) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                return springSecurityUser.getAuthorities().contains(new SimpleGrantedAuthority(authority));
            }
        }
        return false;
    }


    public static List<String> getCurrentUserRoles() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                return springSecurityUser.getAuthorities()
                        .stream().map(m -> m.getAuthority()).collect(Collectors.toList());
            }
        }
        return null;
    }

    public static List<String> getCurrentUserAuthorities() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
                        if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                return springSecurityUser.getAuthorities()
                        .stream().map(m -> m.getAuthority()).collect(Collectors.toList());
            }else if (authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
                OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
                Jwt jwt= JwtHelper.decode(details.getTokenValue());
                if(jwt == null) return null;
                try {
                    BasicUserWithAuthorityDto user = objectMapper.readValue(jwt.getClaims(),BasicUserWithAuthorityDto.class);
                    return user.getAuthorities();
                } catch (IOException e) {
                    log.error("Claim to BasicUserDto error Claim is : {}" ,jwt.getClaims());
                }
            }else if(authentication.getPrincipal() instanceof BasicUserDto){
                return ((BasicUserWithAuthorityDto) authentication.getPrincipal()).getAuthorities();
            }
        }
        return null;
    }

}
