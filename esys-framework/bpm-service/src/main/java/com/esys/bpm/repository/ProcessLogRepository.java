package com.esys.bpm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.esys.bpm.entity.ProcessLog;

public interface ProcessLogRepository extends JpaRepository<ProcessLog, Long>, JpaSpecificationExecutor<ProcessLog> {

	List<ProcessLog> findByProcessLogId(Long processLogId);
}
