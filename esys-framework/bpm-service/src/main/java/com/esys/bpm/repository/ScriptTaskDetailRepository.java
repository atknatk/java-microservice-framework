package com.esys.bpm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.esys.bpm.entity.ScriptTaskDetail;

public interface ScriptTaskDetailRepository
		extends JpaRepository<ScriptTaskDetail, Long>, JpaSpecificationExecutor<ScriptTaskDetail> {

}
