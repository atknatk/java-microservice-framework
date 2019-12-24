package com.esys.bpm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.esys.bpm.entity.NotificationDetail;
import com.esys.bpm.entity.NotificationTask;
import com.esys.bpm.entity.UserTask;

public interface NotificationDetailRepository extends JpaRepository<NotificationDetail, Long>,JpaSpecificationExecutor<NotificationDetail>{

	List<NotificationDetail> findByUserTask(UserTask userTask);
	List<NotificationDetail> findByNotificationTask(NotificationTask notificationTask);
}