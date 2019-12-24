package com.esys.bpm.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
/* @Table(schema = "bpm") */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScriptTaskDetail {
	@Id
	@SequenceGenerator(name = "script_task_detail_id_generator", sequenceName = "script_task_detail_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "script_task_detail_id_generator")
	private Long scriptTaskDetailId;

	@Column(nullable = true)
	private String variableName;
	@Column(nullable = true)
	private String newValue;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE }, optional = false)
	@JoinColumn(name = "scriptTaskId", nullable = false)
	private ScriptTask scriptTask;
}
