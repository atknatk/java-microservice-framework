package com.esys.framework.organization.converter;

import com.esys.framework.core.converter.TypeMapConfigurer;
import com.esys.framework.core.dto.organization.CompanyDto;
import com.esys.framework.core.entity.organization.Company;
import com.esys.framework.core.entity.organization.Group;
import org.modelmapper.Provider;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CompanyMappingConfiguration extends TypeMapConfigurer<Company, CompanyDto> {

    @Override
    public void toDto(TypeMap<Company, CompanyDto> typeMap) {
        typeMap.addMapping(src -> src.getGroup().getId(), CompanyDto::setIdGroup);
    }

    @Override
    public void toEntity(TypeMap<CompanyDto, Company> typeMap) {
        Provider<Group> groupProvider = req -> new Group();
        typeMap.addMappings(mapper -> mapper.<Long>map(src -> src.getIdGroup(), (dest, v) -> dest.getGroup().setId(v)));
    }
}
