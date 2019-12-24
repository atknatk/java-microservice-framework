package com.esys.bpm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.esys.bpm.entity.BusinessRuleTask;

public interface BusinessRuleTaskRepository
		extends JpaRepository<BusinessRuleTask, Long>, JpaSpecificationExecutor<BusinessRuleTask> {

}