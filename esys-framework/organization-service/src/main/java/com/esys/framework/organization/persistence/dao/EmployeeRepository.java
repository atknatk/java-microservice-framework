package com.esys.framework.organization.persistence.dao;

import com.esys.framework.core.entity.organization.Department;
import com.esys.framework.core.entity.organization.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findEmployeesByDepartment (Department department);

}
