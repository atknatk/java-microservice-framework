package com.esys.framework.organization.converter;

import com.esys.framework.core.converter.TypeMapConfigurer;
import com.esys.framework.core.dto.organization.BranchOfficeDto;
import com.esys.framework.core.entity.organization.BranchOffice;
import com.esys.framework.core.entity.organization.Company;
import org.modelmapper.Provider;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BranchOfficeMappingConfiguration extends TypeMapConfigurer<BranchOffice, BranchOfficeDto> {

    @Override
    public void toDto(TypeMap<BranchOffice, BranchOfficeDto> typeMap) {
        typeMap.addMapping(src -> src.getCompany().getId(), BranchOfficeDto::setIdCompany);
    }

    @Override
    public void toEntity(TypeMap<BranchOfficeDto, BranchOffice> typeMap) {
        Provider<Company> companyProvider = req -> new Company();
        typeMap.addMappings(mapper -> mapper.<Long>map(src -> src.getIdCompany(), (dest, v) -> dest.getCompany().setId(v)));
    }
}
