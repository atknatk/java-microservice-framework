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
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DraftComponent {

	@Id
	@SequenceGenerator(name = "draft_component_generator", sequenceName = "draft_component_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "draft_component_id_generator")
	private Long draftComponentId;

	@Column(nullable = false)
	private String xml;

	@Column(columnDefinition = "text", nullable = false)
	private String xmlId;

	@Column(nullable = false)
	private Long draftId;
}
