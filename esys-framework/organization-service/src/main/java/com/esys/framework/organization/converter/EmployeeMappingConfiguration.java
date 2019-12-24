package com.esys.framework.organization.converter;

import com.esys.framework.core.converter.TypeMapConfigurer;
import com.esys.framework.core.dto.organization.EmployeeDto;
import com.esys.framework.core.entity.organization.BranchOffice;
import com.esys.framework.core.entity.organization.Employee;
import org.modelmapper.Provider;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmployeeMappingConfiguration extends TypeMapConfigurer<Employee, EmployeeDto> {


    @Override
    public void toDto(TypeMap<Employee, EmployeeDto> typeMap) {
        typeMap.addMapping(src -> src.getDepartment().getId(), EmployeeDto::setIdDepartment);
    }

    @Override
    public void toEntity(TypeMap<EmployeeDto, Employee> typeMap) {
        Provider<BranchOffice> branchOfficeProvider = req -> new BranchOffice();
        typeMap.addMappings(mapper -> mapper.<Long>map(src -> src.getIdDepartment(), (dest, v) -> dest.getDepartment().setId(v)));
    }
}
