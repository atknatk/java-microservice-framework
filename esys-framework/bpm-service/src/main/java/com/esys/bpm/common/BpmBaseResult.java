package com.esys.bpm.common;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.esys.bpm.common.enums.MessageType;
import com.esys.bpm.common.enums.OperationResult;
import com.esys.bpm.utils.StringUtil;

import lombok.Getter;
import lombok.Setter;

public class BpmBaseResult<T> {
	@Getter
	@Setter
	private T data;

	// Operation Result
	@Getter
	@Setter
	private OperationResult operationResult;

	// Contains Return Parameters
	@Getter
	@Setter
	private List<BpmLookup> returnParameters;

	// Contains All Messages
	@Getter
	private List<BpmMessage> messages;

	// Getters for Message Lists
	public List<BpmMessage> getErrorMessages() {
		return messages.stream().filter(i -> i.type.equals(MessageType.ERROR)).collect(Collectors.toList());
	}

	public List<BpmMessage> getWarningMessages() {
		return messages.stream().filter(i -> i.type.equals(MessageType.WARNING)).collect(Collectors.toList());
	}

	public List<BpmMessage> getInfoMessages() {
		return messages.stream().filter(i -> i.type.equals(MessageType.INFO)).collect(Collectors.toList());
	}

	public boolean hasErrors() {
		return getErrorMessages().size() > 0;
	}

	public boolean hasWarnings() {
		return getWarningMessages().size() > 0;
	}

	public boolean hasInfos() {
		return getInfoMessages().size() > 0;
	}

	public boolean isSuccessful() {
		return !hasErrors() && operationResult != OperationResult.Error;
	}

	public BpmBaseResult() {
		operationResult = OperationResult.NotStartedYet;
		messages = new ArrayList<BpmMessage>();
		returnParameters = new ArrayList<BpmLookup>();
	}

	public BpmLookup getReturnParameter(String code) {
		return returnParameters.stream().filter(i -> i.code.equals(code)).findAny().orElse(null);
	}

	public String getReturnParameterValue(String code) {
		BpmLookup lookup = getReturnParameter(code);
		return lookup == null ? null : lookup.value;
	}

	public boolean AddReturnParameter(String code, String value) {
		return returnParameters.add(new BpmLookup(code, value));
	}

	public boolean RemoveReturnParameter(String code) {
		return returnParameters.remove(getReturnParameter(code));
	}

	public boolean addMessages(List<BpmMessage> messages) {
		if (messages != null) {
			return this.messages.addAll(messages);
		}
		return false;
	}

	public void AddMessage(MessageType type, List<String> messages) {
		if (messages == null) {
			return;
		}

		for (String message : messages) {
			if (StringUtil.isNotNullAndNotEmpty(message))
				this.messages.add(new BpmMessage(type, null, message));
		}
	}

	public void addMessage(BpmMessage message) {
		if (message != null && StringUtil.isNotNullAndNotEmpty(message.getMessage())) {
			messages.add(message);
		}
	}

	public void addErrorMessage(String code, String message) {
		addMessage(new BpmMessage(MessageType.ERROR, code, message));
	}

	public void addErrorMessage(String message) {
		addMessage(new BpmMessage(MessageType.ERROR, null, message));
	}

	public void addWarningMessage(String code, String message) {
		addMessage(new BpmMessage(MessageType.WARNING, code, message));
	}

	public void addWarningMessage(String message) {
		addMessage(new BpmMessage(MessageType.WARNING, null, message));
	}

	public void addInfoMessage(String message) {
		addMessage(new BpmMessage(MessageType.INFO, null, message));
	}

	public void addSuccessMessage(String message) {
		addMessage(new BpmMessage(MessageType.SUCCESS, null, message));
	}

	public void deleteMessages(MessageType messageType) {
		messages.clear();
	}
}
