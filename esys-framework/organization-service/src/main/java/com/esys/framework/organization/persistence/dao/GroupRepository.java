package com.esys.framework.organization.persistence.dao;

import com.esys.framework.core.entity.organization.Group;
import com.esys.framework.core.entity.organization.MainGroup;
import com.esys.framework.core.entity.uaa.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long>{

    List<Group> findGroupsByMainGroup(MainGroup mainGroup);

    Optional<Group> findByNameAndMainGroup(String name, MainGroup mainGroup);

}
