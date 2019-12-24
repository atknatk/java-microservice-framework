package com.esys.bpm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.esys.bpm.entity.TimerTask;

public interface TimerTaskRepository extends JpaRepository<TimerTask, Long>, JpaSpecificationExecutor<TimerTask> {

}
