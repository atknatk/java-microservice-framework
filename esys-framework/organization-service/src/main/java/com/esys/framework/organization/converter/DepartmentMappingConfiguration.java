package com.esys.framework.organization.converter;

import com.esys.framework.core.converter.TypeMapConfigurer;
import com.esys.framework.core.dto.organization.DepartmentDto;
import com.esys.framework.core.entity.organization.Department;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DepartmentMappingConfiguration extends TypeMapConfigurer<Department, DepartmentDto> {


    @Override
    public void toDto(TypeMap<Department, DepartmentDto> typeMap) {
        typeMap.addMapping(src -> src.getBranchOffice().getId(), DepartmentDto::setIdBranchOffice);
    }

    @Override
    public void toEntity(TypeMap<DepartmentDto, Department> typeMap) {
        typeMap.addMappings(mapper -> mapper.<Long>map(src -> src.getIdBranchOffice(), (dest, v) -> dest.getBranchOffice().setId(v)));
    }
}
