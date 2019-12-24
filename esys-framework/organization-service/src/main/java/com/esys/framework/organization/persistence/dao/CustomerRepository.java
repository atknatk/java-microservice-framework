package com.esys.framework.organization.persistence.dao;

import com.esys.framework.core.entity.organization.Customer;
import com.esys.framework.core.entity.organization.Employee;
import com.esys.framework.core.entity.uaa.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findCustomersByEmployee (Employee employee);
    Optional<Customer> findByNameAndEmployee(String name, Employee branchOffice);



}
