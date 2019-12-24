package com.esys.bpm.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.esys.bpm.enums.ProcessComponent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
/* @Table(schema = "bpm") */
@Data
@AllArgsConstructor
@NoArgsConstructor
//Request logic in Oracle Bpm
public class Instance {
	@Id
	@SequenceGenerator(name = "instance_id_generator", sequenceName = "instance_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "instance_id_generator")
	private Long instanceId;

	// Her bir sürecin instance'ları 1'den başlayacağı için
	private long instanceDisplayId;

	private int currentTaskId;
	private int currentUserId;

	@Enumerated(EnumType.STRING)
	private ProcessComponent processComponent;

	private long createUserId;

	private LocalDateTime createDate;
	private LocalDateTime assignDate;

	private int currentEndTaskId;
	private boolean isCompleted;

	@OneToMany(mappedBy = "instance")
	private List<InstanceLog> logs;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE }, optional = false)
	@JoinColumns({ @JoinColumn(name = "processId", referencedColumnName = "id", nullable = false),
			@JoinColumn(name = "process_version", referencedColumnName = "version", nullable = false) })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Process process;

}
