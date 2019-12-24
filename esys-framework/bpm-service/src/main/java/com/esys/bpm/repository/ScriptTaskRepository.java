package com.esys.bpm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.esys.bpm.entity.ScriptTask;

public interface ScriptTaskRepository extends JpaRepository<ScriptTask, Long>, JpaSpecificationExecutor<ScriptTask> {

}
