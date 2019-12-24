package com.esys.framework.core.dto.uaa;

import com.esys.framework.core.entity.uaa.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
public class UserPrincipalDto implements UserDetails {

    private UserDto userDto;

    public UserPrincipalDto(UserDto userDto) {
        this.userDto = userDto;
    }

    @Override
    public String getUsername() {
        return userDto.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {return userDto.isEnabled();}

    @Override
    public String getPassword() {
        return userDto.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (RoleDto roleDto : userDto.getRoles()) {
            for (AuthorityDto authorityDto : roleDto.getAuthorities()) {
                authorities.add(new SimpleGrantedAuthority(authorityDto.getName()));
            }
        }
        return authorities;
    }

}
