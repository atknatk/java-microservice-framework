package com.esys.bpm.dto.diagram;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.esys.bpm.entity.diagram.Label;
import com.esys.bpm.utils.CommonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)

@XmlRootElement(name = "Label")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class LabelDto {

	@XmlAttribute
	private String id;

	@XmlElement(name = "Bounds")
	private BoundsDto bounds;

	public Label toEntity() {

		Label entity = new Label();

		entity.setLabelId(CommonUtil.parseLongElseNull(this.getId()));
		entity.setBounds(this.getBounds().toEntity());

		return entity;
	}

	public static LabelDto toDto(final Label entity) {

		LabelDto dto = new LabelDto();
		dto.setId(entity.getLabelId().toString());
		dto.setBounds(BoundsDto.toDto(entity.getBounds()));

		return dto;
	}
}
