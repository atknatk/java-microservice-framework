package com.esys.bpm.dto.diagram;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.esys.bpm.entity.diagram.Bounds;
import com.esys.bpm.utils.CommonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)

@XmlRootElement(name = "Bounds")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class BoundsDto {

	@XmlAttribute
	private String id;
	@XmlAttribute
	private String x;
	@XmlAttribute
	private String y;
	@XmlAttribute
	private String width;
	@XmlAttribute
	private String height;

	public Bounds toEntity() {

		Bounds entity = new Bounds();

		entity.setBoundsId(CommonUtil.parseLongElseNull(this.getId()));

		entity.setX(this.getX());
		entity.setY(this.getY());
		entity.setWidth(this.getWidth());
		entity.setHeight(this.getHeight());

		return entity;
	}

	public static BoundsDto toDto(final Bounds entity) {

		BoundsDto dto = new BoundsDto();

		dto.setId(entity.getBoundsId().toString());

		dto.setX(entity.getX());
		dto.setY(entity.getY());
		dto.setWidth(entity.getWidth());
		dto.setHeight(entity.getHeight());

		return dto;
	}

}
