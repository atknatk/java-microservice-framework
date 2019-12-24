package com.esys.bpm.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
/*@Table(schema = "bpm")*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstanceLog {
	@Id
	@SequenceGenerator(name = "instance_log_id_generator", sequenceName = "instance_log_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "instance_log_id_generator")
	private Long instanceLogId;
	private String explanation;
	private Long userId;
	private LocalDateTime logDate;
	private String status;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE }, optional = false)
	@JoinColumn(name = "instanceId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Instance instance;
}
