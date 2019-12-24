package com.esys.bpm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.esys.bpm.entity.ServiceTask;

public interface ServiceTaskRepository extends JpaRepository<ServiceTask, Long>, JpaSpecificationExecutor<ServiceTask> {

}
