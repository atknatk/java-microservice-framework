package com.esys.framework.uaa.repository;

import com.esys.framework.core.entity.uaa.Role;
import com.esys.framework.core.entity.uaa.User;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IRoleRepository extends DataTablesRepository<Role, Long> {

    @Query("SELECT r FROM Role r WHERE r.isDefault = :default")
    Set<Role> findRolesByDefault(@Param("default") boolean _default);

    @Query("SELECT u.roles FROM User u WHERE u.email = :email")
    Set<Role> getRolesByUserEmail(@Param("email") String email);

    @Override
    void delete(Role role);

    Optional<Role> findById(long id);

    Optional<Role> findByName(String name);

    boolean existsByName(String name);

}
