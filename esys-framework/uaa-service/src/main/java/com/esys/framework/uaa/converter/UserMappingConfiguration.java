package com.esys.framework.uaa.converter;

import com.esys.framework.core.converter.TypeMapConfigurer;
import com.esys.framework.core.dto.uaa.RoleDto;
import com.esys.framework.core.dto.uaa.UserDto;
import com.esys.framework.core.entity.uaa.Role;
import com.esys.framework.core.entity.uaa.User;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserMappingConfiguration extends TypeMapConfigurer<User, UserDto> {


    @Override
    public void toDto(TypeMap<User, UserDto> typeMap) {
        typeMap.addMapping(src -> src.getRoles(), UserDto::setRoles);
    }

    @Override
    public void toEntity(TypeMap<UserDto, User> typeMap) {
    /*    typeMap.addMappings(mapper -> mapper.<Long>map(src -> src.getAuthorities(),
                (dest, v) -> dest.setAuthorities(modelMapper.map(v, new TypeToken<List<Authority>>() {}.getType()))));
*/    }
}
