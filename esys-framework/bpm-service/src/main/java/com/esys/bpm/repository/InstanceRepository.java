package com.esys.bpm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.esys.bpm.entity.Instance;

public interface InstanceRepository extends JpaRepository<Instance, Long>, JpaSpecificationExecutor<Instance> {

	Instance findByInstanceId(Long instanceId);

	// List<Instance> findByProcessId(ProcessId processId);

	// List<Instance> findByProcessIdAndIsCompleted(ProcessId processId, boolean
	// isCompleted);
}
