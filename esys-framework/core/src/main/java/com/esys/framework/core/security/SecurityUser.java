package com.esys.framework.core.security;

import com.esys.framework.core.dto.uaa.BasicUserDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@Getter
@Setter
class SecurityUser extends BasicUserDto {

    private Collection<String> authorities;

}
