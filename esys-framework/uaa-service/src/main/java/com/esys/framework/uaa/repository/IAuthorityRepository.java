package com.esys.framework.uaa.repository;

import com.esys.framework.core.entity.uaa.Authority;
import com.esys.framework.core.entity.uaa.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findByName(String name);

    List<Authority> getAuthoritiesByRoles(List<Role> roles);

    @Override
    void delete(Authority privilege);

}
