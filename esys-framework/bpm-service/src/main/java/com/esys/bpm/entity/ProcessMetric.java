package com.esys.bpm.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import com.esys.bpm.enums.ProcessStateType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* @Table(schema = "bpm") */
/*@Entity*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessMetric {
	@Id
	@SequenceGenerator(name = "process_metric_id_generator", sequenceName = "process_metric_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "process_metric_id_generator")
	private Long processMetricId;

	@Column(nullable = false)
	private Long createUserId;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	ProcessStateType state;

	@Column(nullable = false)
	private LocalDateTime startDate;
	@Column(nullable = true)
	private LocalDateTime completeDate;
	@Column(nullable = true)
	private Long duration;
	@Column(nullable = true)
	private Long bcDuration;

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
	 * private long requestId; private String createUser; private String
	 * compositeName; private String compositeInstanceId; private String state;
	 * private String activityName; private LocalDateTime processStartDate; private
	 * LocalDateTime processCompleteDate; private int duration; private int
	 * bcDuration;
	 */
}
