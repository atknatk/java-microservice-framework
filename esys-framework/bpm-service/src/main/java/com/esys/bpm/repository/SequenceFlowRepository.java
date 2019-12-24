package com.esys.bpm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.esys.bpm.entity.Process;
import com.esys.bpm.entity.SequenceFlow;

public interface SequenceFlowRepository
		extends JpaRepository<SequenceFlow, Long>, JpaSpecificationExecutor<SequenceFlow> {

	List<SequenceFlow> findAllByProcess(Process process);
}
