package com.esys.bpm.common;

import com.esys.bpm.enums.ProcessComponent;

import lombok.Getter;

@Getter
public class SequenceFlowValidation {

	private ProcessComponent component;

	private String xmlId;
	private int sourceRefCount;
	private int targetRefCount;

	public SequenceFlowValidation(ProcessComponent component, String xmlId) {
		this.component = component;
		this.xmlId = xmlId;
		this.sourceRefCount = 0;
		this.targetRefCount = 0;
	}

	public void increaseSourceRefCount() {
		this.sourceRefCount++;
	}

	public void increaseTargetRefCount() {
		this.targetRefCount++;
	}
}
