package com.esys.framework.organization.persistence.dao;

import com.esys.framework.core.entity.organization.BranchOffice;
import com.esys.framework.core.entity.organization.Company;
import com.esys.framework.core.entity.uaa.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchOfficeRepository extends JpaRepository<BranchOffice, Long> {

    List<BranchOffice> findBranchOfficesByCompany(Company company);

    Optional<BranchOffice> findByNameAndCompany(String name, Company company);


}
