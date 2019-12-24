package com.esys.framework.uaa.service.impl;

import com.codahale.metrics.annotation.Timed;
import com.esys.framework.core.consts.ResultStatusCode;
import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.dto.uaa.RoleDto;
import com.esys.framework.core.dto.uaa.UserDto;
import com.esys.framework.core.entity.uaa.Role;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.entity.uaa.UserGroup;
import com.esys.framework.core.exceptions.ResourceNotFoundException;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.core.repository.IUserRepository;
import com.esys.framework.uaa.entity.PasswordResetToken;
import com.esys.framework.uaa.entity.VerificationToken;
import com.esys.framework.uaa.registration.OnRegistrationCompleteEvent;
import com.esys.framework.uaa.repository.*;
import com.esys.framework.uaa.service.IUserDetailsService;
import com.esys.framework.uaa.service.IUserGroupService;
import com.esys.framework.uaa.service.IUserService;
import com.esys.framework.uaa.web.error.UserAlreadyExistException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class UserService implements IUserService, IUserDetailsService {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IUserRepository repository;

    @Autowired
    private IUserGroupService userGroupService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IPasswordResetTokenRepository passwordTokenRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private IVerificationTokenRepository tokenRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";



    @Override
    public User getUserByEmail(String email) {
        return repository.getUserByEmailAndDeletedIsFalse(email);
    }

    @Override
    public User getUserByEmailAndDomain(String email,String domain) {
        return repository.getUser(email,domain);
    }

    @Override
    @PreAuthorize("hasAuthority('authority.user.new')")
    public UserDto saveUser(UserDto accountDto) {
        final User user = mapper.map(accountDto, User.class);
        user.setLastPasswordModifiedDate(LocalDateTime.now());
        UserGroup userGroup  = userGroupService.saveUserGroupIfNotExist(new UserGroup(accountDto.getEmail().split("@")[1]));
        user.setUserGroups(Arrays.asList(userGroup));
        if(accountDto.isSendActivationEmail()){
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(accountDto, LocaleContextHolder.getLocale(), getAppUrl(request)));
        }
        final User savedUser = repository.save(user);
        return this.mapper.map(savedUser,UserDto.class) ;
    }


    @Override
    public UserDto registerNewUserAccount(UserDto accountDto) throws UserAlreadyExistException {
        if (emailExist(accountDto.getEmail())) {
            throw new UserAlreadyExistException("user.allreadyexists");
        }
        final User user = mapper.map(accountDto, User.class);

        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setLastPasswordModifiedDate(LocalDateTime.now());

        if(accountDto.getRoles() == null || accountDto.getRoles().size() == 0){
            user.setRoles(roleRepository.findRolesByDefault(true));
        }
        UserGroup userGroup  = userGroupService.saveUserGroupIfNotExist(new UserGroup(accountDto.getEmail().split("@")[1]));
        user.setUserGroups(Arrays.asList(userGroup));
        if(accountDto.isSendActivationEmail()){
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(accountDto, LocaleContextHolder.getLocale(), getAppUrl(request)));
        }
        User savedUser = repository.save(user);
        return mapper.map(savedUser, UserDto.class);
    }

    @Override
    public void createVerificationTokenForUser(UserDto userDto, String token) {
        final User user = mapper.map(userDto, User.class);
        final VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }



    @Override
    public String validateVerificationToken(String token) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            tokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }

        user.setEnabled(true);
        repository.save(user);
        return TOKEN_VALID;
    }


    @Override
    @Transactional
    public User getUserFromVerificationToken(final String verificationToken) {
        final VerificationToken token = tokenRepository.findByToken(verificationToken);
        if (token != null) {
            if(!token.getUser().isConfirmEmail()){
                token.getUser().setConfirmEmail(true);
                repository.save(token.getUser());
            }
            return repository.findByIdWithRoles(token.getUser().getId()).get();
        }
        return null;
    }

    @Override
    public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
        VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
        vToken.updateToken(UUID.randomUUID().toString());
        vToken = tokenRepository.save(vToken);
        return vToken;
    }


    @Override
    public void createPasswordResetTokenForUser(final User user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }


    @Override
    public String validatePasswordResetToken(long id, String token) {
        final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
        if ((passToken == null) || (passToken.getUser().getId() != id)) {
            return "invalidToken";
        }

        final Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return "expired";
        }

        final User user = passToken.getUser();
        final Authentication auth = new UsernamePasswordAuthenticationToken(user, null, Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return null;
    }

    @Override
    public void changeUserPassword(final User user, final String password) {
        user.setPassword(passwordEncoder.encode(password));
        user.setLastPasswordModifiedDate(LocalDateTime.now());
        repository.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(final User user, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public ModelResult assignRoles(final User user, final List<Role> roles) {
        User dbUser = repository.getUserByEmailAndDeletedIsFalse(user.getEmail());
        dbUser.setRoles(roles);
        try {
            repository.save(dbUser);
            return  new ModelResult.ModelResultBuilders()
                    .setMessageKey("uaa.updateRolesSuc", LocaleContextHolder.getLocale())
                    .success();
        }catch (Exception ex){
            return new ModelResult.ModelResultBuilders().addError(ex.getLocalizedMessage())
                    .setStatus(-1)
                    .build();
        }

    }

    @Override
    public ModelResult assignRole(Long idUser, Long idRole) {
        Optional<User> user = repository.findByIdWithRoles(idUser);

        if(!user.isPresent()){
            throw new ResourceNotFoundException("user.notFound");
        }

        Optional<Role> role = roleRepository.findById(idRole);

        if(!role.isPresent()){
            throw new ResourceNotFoundException("authority.notFound");
        }
        if(user.get().getRoles().stream().anyMatch(p -> p.getId().equals(idRole))){
            throw new ResourceNotFoundException("authority.assign.allready");
        }

        user.get().getRoles().add(new Role(idRole));
        try{
            repository.save(user.get());
            return new ModelResult.ModelResultBuilders()
                    .setMessageKey("uaa.updateRolesSuc", LocaleContextHolder.getLocale())
                    .success();
        }catch (Exception ex){
            log.error("assignRole error" ,ex);
            return new ModelResult.ModelResultBuilders()
                    .addError(ex.getMessage())
                    .setStatus(ResultStatusCode.UNKNOWN_ERROR)
                    .build();
        }
    }


    @Override
    public ModelResult removeRole(Long idUser, Long idRole) {
        Optional<User> user = repository.findByIdWithRoles(idUser);

        if(!user.isPresent()){
            throw new ResourceNotFoundException("user.notFound");
        }

        Optional<Role> role = roleRepository.findById(idRole);

        if(!role.isPresent()){
            throw new ResourceNotFoundException("authority.notFound");
        }

        if(!user.get().getRoles().stream().anyMatch(p -> p.getId().equals(idRole))){
            throw new ResourceNotFoundException("authority.assign.notallready");
        }

        Collection<Role>  roles = user.get().getRoles().stream().filter(p -> p.getId() != idRole)
                .collect(Collectors.toList());

        user.get().setRoles(roles);

        try{
            repository.save(user.get());
            return new ModelResult.ModelResultBuilders()
                    .setMessageKey("uaa.updateRolesSuc", LocaleContextHolder.getLocale())
                    .success();
        }catch (Exception ex){
            log.error("remove authority error" ,ex);
            return new ModelResult.ModelResultBuilders()
                    .addError(ex.getMessage())
                    .setStatus(ResultStatusCode.UNKNOWN_ERROR)
                    .build();
        }
    }

    @Override
    public UserDto getUserById(Long id) throws ResourceNotFoundException {
        Optional<User> user = repository.findByIdWithRoles(id);

        if(!user.isPresent()){
            throw new ResourceNotFoundException("user.notFound");
        }
        return mapper.map(user.get(),UserDto.class);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('authority.user.new','authority.user.edit','authority.user.delete')")
    public List<UserDto> findAll() {
        List<User> list = repository.findAll();
        List<UserDto> userDtoList =  mapper
                .map(list,new TypeToken<List<UserDto>>() {}.getType());
        return userDtoList;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('authority.user.new','authority.user.edit','authority.user.delete')")
    public List<BasicUserDto> findAllBasic() {
        return repository.findAllBasic();
    }

    @Override
    @PreAuthorize("hasAuthority('authority.user.edit')")
    public UserDto updateUser(UserDto userDto) {
        User user = mapper.map(userDto,User.class);

        if(user.getPassword() == null || user.getPassword().equals("")){
            Optional<User> dbuser =  repository.getUserById(user.getId());
            if(dbuser.isPresent()){
                user.setPassword(dbuser.get().getPassword());
            }
        }else{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setLastPasswordModifiedDate(LocalDateTime.now());
        }

        if(userDto.isSendActivationEmail() && !user.isConfirmEmail()){
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(userDto, LocaleContextHolder.getLocale(), getAppUrl(request)));
        }

        User saved = repository.save(user);
        return mapper.map(saved,UserDto.class);
    }

    @Override
    @PreAuthorize("hasAuthority('authority.user.delete')")
    public UserDto deleteUser(Long userId) {
        Optional<User> user = repository.findById(userId);

        if(!user.isPresent()){
            throw new ResourceNotFoundException("user.notFound");
        }
        repository.deleteById(userId);
        return mapper.map(user.get(),UserDto.class);
    }

    @Override
    public void updateLastLoginTime(Long userId) {
        repository.updateLastLoginTime(userId);
    }

    @Override
    public void updateLoginAttemptTryCount(Long userId) {
        repository.updateLoginAttemptTryCount(userId);
    }

    @Override
    public void updateEnable(Long userId, boolean enable) {
        repository.updateEnable(userId,enable);
    }

    @Override
    public boolean checkPassword(String mail, String password) {
        String dbPassword = repository.getPassword(mail);
        return passwordEncoder.matches(password, dbPassword);
    }

    @Override
    public boolean exist(Long userId) {
        return repository.existsById(userId);
    }

    @Override
    @Timed
    //@JsonView(DataTablesOutput.View.class)
    @PreAuthorize("hasAnyAuthority('authority.user.new','authority.user.edit','authority.user.delete')")
    public DataTablesOutput<UserDto> usersPaging(@Valid DataTablesInput input) {
       // input.addColumn("deleted",false,false,"false");
        Function<User, UserDto> toDto = user -> toDto(user);
        DataTablesOutput<UserDto> output = repository.findAll(input,toDto);
        return output;
    }

    @Override
    public DataTablesOutput<BasicUserDto> usersPagingBasic(@Valid DataTablesInput input) {
        Function<User, BasicUserDto> toDto = user -> toBasicDto(user);
        DataTablesOutput<BasicUserDto> output = repository.findAll(input,toDto);
        return output;
    }


    private boolean emailExist(final String email) {
        return repository.getUserByEmailAndDeletedIsFalse(email) != null;
    }

    @Override
    public UserDetails loadUserByUsernameAndDomain(String username, String domain) throws UsernameNotFoundException {
        User userDetails = getUserByEmailAndDomain(username,domain);
        if(userDetails == null){
            throw  new UsernameNotFoundException("Kullanıcı Yok");
        }

       return userDetails;
    }


    private User toEntity(UserDto dto){
        log.debug("UserDto converting to User {}",dto);
        final User converted = mapper.map(dto, User.class);
        log.debug("UserDto converted to User {}",converted);
        return converted;
    }

    private UserDto toDto(User user){
        log.debug("User converting to UserDto {}",user);
        final UserDto converted = mapper.map(user, UserDto.class);
        log.debug("User converted to UserDto {}",converted);
        return converted;
    }

    private BasicUserDto toBasicDto(User user){
        log.debug("User converting to BasicUserDto {}",user);
        final BasicUserDto converted = mapper.map(user, BasicUserDto.class);
        log.debug("User converted to BasicUserDto {}",converted);
        return converted;
    }


    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
