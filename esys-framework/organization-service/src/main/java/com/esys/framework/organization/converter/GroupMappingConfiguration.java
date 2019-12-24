package com.esys.framework.organization.converter;

import com.esys.framework.core.converter.TypeMapConfigurer;
import com.esys.framework.core.dto.organization.GroupDto;
import com.esys.framework.core.entity.organization.Group;
import com.esys.framework.core.entity.organization.MainGroup;
import org.modelmapper.Provider;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GroupMappingConfiguration  extends TypeMapConfigurer<Group, GroupDto> {

    @Override
    public void toDto(TypeMap<Group, GroupDto> typeMap) {
        typeMap.addMapping(src -> src.getMainGroup().getId(), GroupDto::setIdMainGroup);
    }

    @Override
    public void toEntity(TypeMap<GroupDto, Group> typeMap) {
        Provider<MainGroup> mainGroupProvider = req -> new MainGroup();
        typeMap.addMappings(mapper -> mapper.<Long>map(src -> src.getIdMainGroup(), (dest, v) -> dest.getMainGroup().setId(v)));
    }
}
