package com.esys.framework.uaa.converter;

import com.esys.framework.core.converter.TypeMapConfigurer;
import com.esys.framework.core.dto.uaa.RoleDto;
import com.esys.framework.core.entity.uaa.Authority;
import com.esys.framework.core.entity.uaa.Role;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RoleMappingConfiguration extends TypeMapConfigurer<Role, RoleDto> {

/*
    @Autowired
    private ModelMapper modelMapper;
*/

    @Override
    public void toDto(TypeMap<Role, RoleDto> typeMap) {
        typeMap.addMapping(src -> src.getAuthorities(), RoleDto::setAuthorities);
    }

    @Override
    public void toEntity(TypeMap<RoleDto, Role> typeMap) {
    /*    typeMap.addMappings(mapper -> mapper.<Long>map(src -> src.getAuthorities(),
                (dest, v) -> dest.setAuthorities(modelMapper.map(v, new TypeToken<List<Authority>>() {}.getType()))));
*/    }
}
