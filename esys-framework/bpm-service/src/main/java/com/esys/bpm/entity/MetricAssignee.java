package com.esys.bpm.entity;

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
public class MetricAssignee {
	@Id
	@SequenceGenerator(name = "assigneeMetric_id_generator", sequenceName = "assigneeMetric_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "assigneeMetric_id_generator")
	private Long id;

	private Long instanceId;
	private int taskNumber;
	private boolean isCompleted;
	private ProcessId processId;
	private Participant assignee;
	/*
	 * private long requestId; private String compositeName; private int thread;
	 * private String assignee; private String assigneeType;
	 */
}
