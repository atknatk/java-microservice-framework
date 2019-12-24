package com.esys.bpm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
/* @Table(schema = "bpm") */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScriptAction {
	@Id
	@SequenceGenerator(name = "script_action_id_generator", sequenceName = "script_action_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "script_action_id_generator")
	private Long scriptActionId;
	@Column(nullable = true)
	private Long scriptTaskId;
	@Column(nullable = true)
	private Long objectReferanceId;
	@Column(nullable = true)
	private String action;
}
