package com.esys.bpm.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

import com.esys.bpm.enums.ExpressionType;
import com.esys.bpm.enums.RoleType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
/* @Table(schema = "bpm") */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Participant {

	@Id
	@SequenceGenerator(name = "participant_id_generator", sequenceName = "participant_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "participant_id_generator")
	private Long participantId;

	// userId, userGroupId, bpmRoleId and systemRoleId are used if expressionType is
	// name
	@Column(nullable = true)
	private Long userId;
	@Column(nullable = true)
	private Long userGroupId;
	@Column(nullable = true)
	private Long bpmRoleId;
	@Column(nullable = true)
	private Long systemRoleId;

	// Used if expressionType is not name
	@Column(nullable = true)
	private String participantName;

	@Enumerated(EnumType.STRING)
	private RoleType roleType;

	@Enumerated(EnumType.STRING)
	private ExpressionType expressionType;

	// optional = true because foreign keys should be null
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE }, optional = true)
	@JoinColumns({ @JoinColumn(name = "processId", referencedColumnName = "id", nullable = true),
			@JoinColumn(name = "process_version", referencedColumnName = "version", nullable = true) })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Process process;

	// optional = true because foreign keys should be null
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE }, optional = true)
	@JoinColumn(name = "userTaskId", nullable = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private UserTask userTask;

	// optional = true because foreign keys should be null
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE }, optional = true)
	@JoinColumn(name = "toTargetId", referencedColumnName = "notificationDetailId", nullable = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private NotificationDetail toTarget;

	// optional = true because foreign keys should be null
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE }, optional = true)
	@JoinColumn(name = "ccTargetId", referencedColumnName = "notificationDetailId", nullable = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private NotificationDetail ccTarget;

	// optional = true because foreign keys should be null
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE }, optional = true)
	@JoinColumn(name = "bccTargetId", referencedColumnName = "notificationDetailId", nullable = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private NotificationDetail bccTarget;
}
