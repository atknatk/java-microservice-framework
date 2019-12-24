package com.esys.bpm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.esys.bpm.entity.NotificationTask;

public interface NotificationTaskRepository
		extends JpaRepository<NotificationTask, Long>, JpaSpecificationExecutor<NotificationTask> {

}
