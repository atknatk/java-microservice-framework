package com.esys.bpm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.esys.bpm.entity.SqlTask;

public interface SqlTaskRepository extends JpaRepository<SqlTask, Long>, JpaSpecificationExecutor<SqlTask> {

}
