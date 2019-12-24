package com.esys.bpm.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetricTask {
	@Id
	@SequenceGenerator(name = "taskMetric_id_generator", sequenceName = "taskMetric_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "taskMetric_id_generator")
	private Long id;
	private Long requestId;
	private String compositeName;
	private int taskNumber;
	private int thread;
	private String activityName;
	private String action;
	private String taskPerformer;
	private String taskOutcome;
	private String taskState;
	private LocalDateTime taskAssignDate;
	private LocalDateTime taskCompleteDate;
	private LocalDateTime taskSlaDate;
	private boolean isCompleted;
	private int duration;
	private int businessCalendarDuration;
	private boolean isSuspended;
	private int previousTaskNumber;
	private String createUser;
	private LocalDateTime createDate;
	private String updateUser;
	private LocalDateTime updateDate;
}
