package com.esys.framework.organization.converter;

import com.esys.framework.core.converter.TypeMapConfigurer;
import com.esys.framework.core.dto.organization.CustomerDto;
import com.esys.framework.core.entity.organization.Customer;
import com.esys.framework.core.entity.organization.Employee;
import org.modelmapper.Provider;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerMappingConfiguration extends TypeMapConfigurer<Customer, CustomerDto> {


    @Override
    public void toDto(TypeMap<Customer, CustomerDto> typeMap) {
        typeMap.addMapping(src -> src.getEmployee().getId(), CustomerDto::setIdEmployee);
    }

    @Override
    public void toEntity(TypeMap<CustomerDto, Customer> typeMap) {
        Provider<Employee> employeeProvider = req -> new Employee();
        typeMap.addMappings(mapper -> mapper.<Long>map(src -> src.getIdEmployee(), (dest, v) -> dest.getEmployee().setId(v)));
    }
}
