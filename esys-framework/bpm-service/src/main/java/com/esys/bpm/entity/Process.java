package com.esys.bpm.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

import com.esys.bpm.enums.ProcessStatusType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
/* @Table(schema = "bpm") */
@Data
@AllArgsConstructor
@NoArgsConstructor
//@IdClass( ProcessId.class )
public class Process extends TaskBase {

	public Process(ProcessId processId) {
		this.setProcessId(processId);
	}

	@EmbeddedId
	/*
	 * @SequenceGenerator(name = "process_id_generator", sequenceName =
	 * "process_seq", allocationSize = 1)
	 * 
	 * @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
	 * "process_id_generator")
	 */
	private ProcessId processId;

	@Column(nullable = false)
	private Long createUserId;
	@Column(nullable = false)
	private LocalDateTime createDate;
	@Column(nullable = true)
	private Long updateUserId;
	@Column(nullable = true)
	private LocalDateTime updateDate;
	@Column(nullable = false)
	private Long companyId;

	/*
	 * @Column(nullable = false) private String diagramXml;
	 */

	/////////////////////////////////////////////////// ProcessInfo///////////////////////////////////////////////
	@Enumerated(EnumType.STRING)
	private ProcessStatusType status;

	@OneToMany(mappedBy = "process")
	private List<Participant> admins;

	@OneToMany(mappedBy = "process")
	private List<ProcessLog> logs;

	/////////////////////////////////////////////////// Drag&DropUI///////////////////////////////////////////////
	// Bir process'in sadece 1 tane startTask'ı olabilir ancak subprocesslerin de
	// start task'ı olacağı için OneToMany relation içerir
	@OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
	private List<StartTask> startTasks;

	@OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
	private List<UserTask> userTasks;

	@OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
	private List<EndTask> endTasks;

	@OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
	private List<ServiceTask> serviceTasks;

	@OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
	private List<SqlTask> sqlTasks;

	@OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
	private List<BusinessRuleTask> businessRuleTasks;

	@OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
	private List<Gateway> gateways;

	@OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
	private List<Instance> instances;

	@OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
	private List<ScriptTask> scriptTasks;

	@OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
	private List<NotificationTask> notificationTasks;

	@OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
	private List<TimerTask> timerTasks;

	@OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
	private List<Subprocess> subprocesses;

	@OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
	private List<SequenceFlow> sequenceFlows;

	@OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
	private List<Association> associations;

	@OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
	private List<TextAnnotation> textAnnotations;

}
