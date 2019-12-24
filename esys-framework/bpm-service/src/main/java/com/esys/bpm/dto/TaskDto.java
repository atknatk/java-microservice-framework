package com.esys.bpm.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.esys.bpm.enums.ProcessComponent;

import lombok.Data;
import lombok.EqualsAndHashCode;

//Anything common about all types of tasks goes here!

@Data
@EqualsAndHashCode(callSuper = false)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TaskDto extends BpmBaseDto {

	@XmlTransient
	private ProcessComponent component;

	@XmlElement(name = "incoming")
	private List<String> incomings = new ArrayList<String>();
	@XmlElement(name = "outgoing")
	private List<String> outgoings = new ArrayList<String>();
}
