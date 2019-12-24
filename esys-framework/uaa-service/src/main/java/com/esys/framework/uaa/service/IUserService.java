package com.esys.framework.uaa.service;

import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.dto.uaa.UserDto;
import com.esys.framework.core.entity.uaa.Role;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.exceptions.ResourceNotFoundException;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.uaa.entity.VerificationToken;
import com.esys.framework.uaa.web.error.UserAlreadyExistException;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface IUserService {

    User getUserByEmail(String email);

    User getUserByEmailAndDomain(String email,String domain);

    UserDto saveUser(UserDto user);

    UserDto registerNewUserAccount(UserDto accountDto) throws UserAlreadyExistException;

    void createVerificationTokenForUser(UserDto user, String token);

    String validateVerificationToken(String token);

    User getUserFromVerificationToken(final String verificationToken);

    VerificationToken generateNewVerificationToken(final String existingVerificationToken);

    void createPasswordResetTokenForUser(final User user, final String token);

    String validatePasswordResetToken(long id, String token);

    void changeUserPassword(User user, String password);

    boolean checkIfValidOldPassword(User user, String password);

    ModelResult assignRoles(final User user, final List<Role> roles);

    ModelResult assignRole(final Long idUser, final Long idRole);

    ModelResult removeRole(Long idUser, Long idRole);

    UserDto getUserById(final Long Id) throws ResourceNotFoundException;

    List<UserDto> findAll();

    List<BasicUserDto> findAllBasic();

    UserDto updateUser(UserDto userDto);

    UserDto deleteUser(Long userId);

    void updateLastLoginTime(Long userId);

    void updateLoginAttemptTryCount(Long userId);

    void updateEnable(Long userId,boolean enable);

    boolean checkPassword(String username, String password);

    boolean exist(final Long userId);

    DataTablesOutput<UserDto> usersPaging(@Valid @RequestBody DataTablesInput input);

    DataTablesOutput<BasicUserDto> usersPagingBasic(@Valid @RequestBody DataTablesInput input);

    UserDetails loadUserByUsernameAndDomain(String username, String domain) throws UsernameNotFoundException;
}
