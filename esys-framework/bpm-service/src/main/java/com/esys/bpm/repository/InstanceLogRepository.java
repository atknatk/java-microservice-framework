package com.esys.bpm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.esys.bpm.entity.InstanceLog;

public interface InstanceLogRepository extends JpaRepository<InstanceLog, Long>,JpaSpecificationExecutor<InstanceLog>{
	List<InstanceLog> findByInstanceLogId(Long instanceLogId);
}
