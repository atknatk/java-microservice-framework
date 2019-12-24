package com.esys.framework.organization.persistence.dao;

import com.esys.framework.core.entity.organization.BranchOffice;
import com.esys.framework.core.entity.organization.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findDepartmentsByBranchOffice(BranchOffice branchOffice);

}
