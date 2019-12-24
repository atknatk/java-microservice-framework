package com.esys.framework.uaa.converter;

import com.esys.framework.core.converter.TypeMapConfigurer;
import com.esys.framework.core.dto.uaa.UserDto;
import com.esys.framework.core.dto.uaa.UserGroupDto;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.entity.uaa.UserGroup;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserGroupMappingConfiguration extends TypeMapConfigurer<UserGroup, UserGroupDto> {


    @Override
    public void toDto(TypeMap<UserGroup, UserGroupDto> typeMap) {

    }

    @Override
    public void toEntity(TypeMap<UserGroupDto, UserGroup> typeMap) {
        //typeMap.addMappings(mapper -> mapper.<Long>map(src -> src.getUsers(), (dest, v) -> dest.getUsers().setId(v)));
    }
}
