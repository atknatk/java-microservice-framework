package com.esys.bpm.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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
public class Subprocess {
	@Id
	@SequenceGenerator(name = "subprocess_id_generator", sequenceName = "subprocess_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subprocess_id_generator")
	private Long subprocessId;

	@Column(nullable = false)
	private String xmlId;

	@Column(nullable = false)
	private String name;
	@Column(nullable = true)
	private String description;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE }, optional = false)
	@JoinColumns({ @JoinColumn(name = "id", nullable = false), @JoinColumn(name = "version", nullable = false) })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Process process;
}
