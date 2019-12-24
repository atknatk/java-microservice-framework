package com.esys.bpm.common;

import com.esys.bpm.common.enums.MessageType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BpmMessage {
	
	MessageType type;
	String code;
	String message;
}
