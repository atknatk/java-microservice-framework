package com.esys.bpm.entity;

import java.util.List;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.esys.bpm.enums.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
/* @Table(schema = "bpm") */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDetail {
	@Id
	@SequenceGenerator(name = "notification_detail_id_generator", sequenceName = "notification_detail_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_detail_id_generator")
	private Long notificationDetailId;

	@Column(nullable = true)
	private String fromSource;
	@Column(nullable = true)
	private String subject;
	@Column(nullable = false)
	private String body;

	@OneToMany(mappedBy = "toTarget", cascade = CascadeType.ALL)
	private List<Participant> toTargets;

	@OneToMany(mappedBy = "bccTarget", cascade = CascadeType.ALL)
	private List<Participant> bccTargets;

	@OneToMany(mappedBy = "ccTarget", cascade = CascadeType.ALL)
	private List<Participant> ccTargets;

	@Enumerated(EnumType.STRING)
	private NotificationType notificationType;

	// optional = true because foreign keys should be null
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE }, optional = true)
	@JoinColumn(name = "notificationTaskId", nullable = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private NotificationTask notificationTask;

	// optional = true because foreign keys should be null
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE }, optional = true)
	@JoinColumn(name = "userTaskId", nullable = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private UserTask userTask;
}
