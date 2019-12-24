package com.esys.framework.organization.persistence.dao;

import com.esys.framework.core.entity.organization.MainGroup;
import com.esys.framework.core.entity.uaa.User;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MainGroupRepository extends CrudRepository<MainGroup, Long> {

    Optional<MainGroup> findByName(String name);
}
