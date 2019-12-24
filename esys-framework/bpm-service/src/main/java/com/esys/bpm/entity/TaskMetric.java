package com.esys.bpm.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
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

import com.esys.bpm.enums.TaskActionType;
import com.esys.bpm.enums.TaskStateType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*@Table(schema = "bpm") */
/*@Entity*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskMetric {

	@Id
	@SequenceGenerator(name = "task_metric_id_generator", sequenceName = "task_metric_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_metric_id_generator")
	private Long taskMetricId;

	private Long performerId;
	private String outcome;

	@Enumerated(EnumType.STRING)
	TaskStateType state;

	private LocalDateTime assignDate;
	private LocalDateTime completeDate;
	private LocalDateTime taskSlaDate;

	private boolean isCompleted;

	private Long duration;
	private Long bcDuration;

	// TODO existence consideration
	@Enumerated(EnumType.STRING)
	TaskActionType action;
	// TODO existence consideration
	private boolean isSuspended;
	// TODO existence consideration
	private int previousTaskNumber;

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

}
