package com.esys.bpm.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BpmBaseDto {
	// XML id
	@XmlAttribute(name = "id")
	private String xmlId;

	@XmlAttribute
	private String name;

	@XmlTransient
	private String description;
}
