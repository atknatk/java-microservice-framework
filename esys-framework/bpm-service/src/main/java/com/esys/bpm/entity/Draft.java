package com.esys.bpm.entity;

import java.time.LocalDateTime;

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
public class Draft {
	@Id
	@SequenceGenerator(name = "draft_id_generator", sequenceName = "draft_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "draft_id_generator")
	private Long draftId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Long version;

	@Column(nullable = false)
	private String status;

	@Column(nullable = false)
	private String responsibleUser;

	@Column(nullable = false)
	private String createUser;

	@Column(nullable = false)
	private LocalDateTime createDate;

	@Column(nullable = false)
	private String lastModifiedUser;

	@Column(nullable = false)
	private LocalDateTime lastModifiedDate;

	@Column(columnDefinition = "text", nullable = false)
	private String xml;
}
