package com.esys.framework.core.dto.uaa;


import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.security.field.SecureField;
import com.esys.framework.core.security.field.policy.AuthorityBasedFieldSecurityPolicy;
import com.esys.framework.core.validation.PasswordMatches;
import com.esys.framework.core.validation.ValidEmail;
import com.esys.framework.core.validation.ValidPassword;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@ToString
@PasswordMatches
public class UserDto extends BasicUserDto {


    @ValidPassword()
    @SecureField(policyClasses = AuthorityBasedFieldSecurityPolicy.class,authorities = {"authority.show.password"})
    private String password;

    @JsonProperty("email")
    @ValidEmail
    private String email;

    // @NotNull
    // @Size(min = 1)
    private String matchingPassword;

    private String phone;

    private boolean enabled;

    private boolean sendActivationEmail;

    private boolean shouldChangePasswordOnNextLogin;

    private boolean isAccountNonLocked;


    private boolean confirmEmail=false;

    private boolean isUsing2FA;

    private LocalDateTime lastLoginDate;

    private LocalDateTime createdDate;

    private List<RoleDto> roles;

}

