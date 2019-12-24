package com.esys.bpm.entity;

import java.time.LocalDateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.Data;
import lombok.ToString;

//@Entity
@Data
@ToString
public class MetricProcess {
	@Id
	@SequenceGenerator(name = "processMetric_id_generator", sequenceName = "processMetric_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "processMetric_id_generator")
	private long id;

	private ProcessId processId;
	private Long instanceId;
	private Long createUserId;
	private String state; // error detail, success etc.
	private Long userTaskId;
	private LocalDateTime startDate;
	private LocalDateTime completeDate;
	private int duration;
	private int bcDuration;

	/*
	 * private long requestId; private String createUser; private String
	 * compositeName; private String compositeInstanceId; private String state;
	 * private String activityName; private LocalDateTime processStartDate; private
	 * LocalDateTime processCompleteDate; private int duration; private int
	 * bcDuration;
	 */
}
