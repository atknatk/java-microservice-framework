package com.esys.bpm.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class TaskBase {

	@Column(nullable = false)
	private String name;
	@Column(nullable = true)
	private String description;
	@Column(nullable = false)
	private String xmlId;
}
