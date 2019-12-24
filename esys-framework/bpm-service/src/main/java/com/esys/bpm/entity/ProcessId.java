package com.esys.bpm.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class ProcessId implements Serializable {

	// @SequenceGenerator(name = "process_id_generator", sequenceName =
	// "process_seq", allocationSize = 1)
	// @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
	// "process_id_generator")
	private Long id;
	private int version;

	public ProcessId() {

	}

	public ProcessId(Long id, int version) {
		this.id = id;
		this.version = version;
	}

	public Long getId() {
		return id;
	}

	public int getVersion() {
		return version;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ProcessId))
			return false;
		ProcessId that = (ProcessId) o;
		return Objects.equals(getId(), that.getId()) && Objects.equals(getVersion(), that.getVersion());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getVersion());
	}
}
