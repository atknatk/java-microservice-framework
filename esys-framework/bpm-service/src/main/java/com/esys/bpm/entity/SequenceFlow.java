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

import com.esys.bpm.enums.ProcessComponent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
/* @Table(schema = "bpm") */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SequenceFlow {
	@Id
	@SequenceGenerator(name = "sequence_flow_id_generator", sequenceName = "sequence_flow_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_flow_id_generator")
	private Long sequenceFlowId;

	@Column(nullable = false)
	private String xmlId;

	@Column(nullable = true)
	private Long sourceRefId;
	@Column(nullable = false)
	private String sourceRefXmlId;
	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private ProcessComponent sourceRefComponent;

	@Column(nullable = true)
	private Long targetRefId;
	@Column(nullable = false)
	private String targetRefXmlId;
	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private ProcessComponent targetRefComponent;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE }, optional = false)
	@JoinColumns({ @JoinColumn(name = "processId", referencedColumnName = "id", nullable = false),
			@JoinColumn(name = "process_version", referencedColumnName = "version", nullable = false) })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Process process;
}
