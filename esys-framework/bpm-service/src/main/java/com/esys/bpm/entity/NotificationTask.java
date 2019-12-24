package com.esys.bpm.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
/* @Table(schema = "bpm") */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationTask extends TaskBase {
	@Id
	@SequenceGenerator(name = "notification_task_id_generator", sequenceName = "notification_task_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_task_id_generator")
	private Long notificationTaskId;

	@OneToMany(mappedBy = "notificationTask")
	private List<NotificationDetail> notificationDetails;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE }, optional = false)
	@JoinColumns({ @JoinColumn(name = "processId", referencedColumnName = "id", nullable = false),
			@JoinColumn(name = "process_version", referencedColumnName = "version", nullable = false) })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Process process;
}
