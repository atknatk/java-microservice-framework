package com.esys.bpm.dto.diagram;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.esys.bpm.entity.diagram.Shape;
import com.esys.bpm.utils.CommonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)

@XmlRootElement(name = "Shape")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class ShapeDto {

	@XmlAttribute(name = "id")
	private String xmlId;

	@XmlTransient
	private Long shapeId;

	@XmlAttribute(name = "bpmnElement")
	private String elementXmlId;
	@XmlTransient
	private Long elementId;

	@XmlElement(name = "Bounds")
	private BoundsDto bounds;

	@XmlElement(name = "Label")
	private LabelDto label;

	public Shape toEntity() {

		Shape entity = new Shape();

		entity.setShapeId(CommonUtil.parseIdFromXmlId(this.getXmlId()));
		entity.setXmlId(CommonUtil.parseOriginalXmlIdFromXmlId(this.getXmlId()));

		entity.setElementId(CommonUtil.parseIdFromXmlId(this.getElementXmlId()));
		entity.setElementXmlId(CommonUtil.parseOriginalXmlIdFromXmlId(this.getElementXmlId()));

		entity.setBounds(this.getBounds().toEntity());
		if (this.getLabel() != null)
			entity.setLabel(this.getLabel().toEntity());

		return entity;
	}

	public static ShapeDto toDto(final Shape entity) {

		ShapeDto dto = new ShapeDto();

		dto.setShapeId(entity.getShapeId());
		dto.setXmlId(entity.getShapeId().toString() + "_" + entity.getXmlId());

		dto.setElementId(entity.getElementId());
		dto.setElementXmlId(entity.getElementId().toString() + "_" + entity.getElementXmlId());

		dto.setBounds(BoundsDto.toDto(entity.getBounds()));
		if (entity.getLabel() != null)
			dto.setLabel(LabelDto.toDto(entity.getLabel()));

		return dto;
	}

	public static List<Shape> toEntityList(final List<ShapeDto> dtoList) {
		List<Shape> entityList = new ArrayList<Shape>(0);
		if (dtoList != null) {
			for (ShapeDto dto : dtoList) {
				Shape entity = dto.toEntity();
				entityList.add(entity);
			}
		}
		return entityList;
	}

	public static List<ShapeDto> toDtoList(final List<Shape> entityList) {
		List<ShapeDto> dtoList = new ArrayList<ShapeDto>(0);
		if (entityList != null) {
			for (Shape entity : entityList) {
				ShapeDto dto = ShapeDto.toDto(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
}
