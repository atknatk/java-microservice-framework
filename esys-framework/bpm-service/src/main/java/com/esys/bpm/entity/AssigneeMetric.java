package com.esys.bpm.entity;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*@Table(schema = "bpm") */
/*@Entity*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssigneeMetric {
	@Id
	@SequenceGenerator(name = "assignee_metric_id_generator", sequenceName = "assignee_metric_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "assignee_metric_id_generator")
	private Long assigneeMetricId;

	private boolean isCompleted;
	private ProcessId processId;
	private Participant assignee;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, optional = false)
	@JoinColumn(name = "instanceId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Instance instance;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, optional = false)
	@JoinColumn(name = "userTaskId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private UserTask userTask;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE }, optional = false)
	@JoinColumns({ @JoinColumn(name = "processId", referencedColumnName = "id", nullable = false),
			@JoinColumn(name = "process_version", referencedColumnName = "version", nullable = false) })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Process process;

	/*
	 * private long requestId; private String compositeName; private int thread;
	 * private String assignee; private String assigneeType;
	 */
}
