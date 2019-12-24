package com.esys.bpm.dto.diagram;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.esys.bpm.entity.diagram.Plane;
import com.esys.bpm.utils.CommonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)

@XmlRootElement(name = "Plane")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class PlaneDto {

	@XmlTransient
	private Long planeId;

	@XmlAttribute(name = "id")
	private String xmlId;

	@XmlAttribute(name = "bpmnElement")
	private String element;

	@XmlElement(name = "Shape")
	private List<ShapeDto> shapes = new ArrayList<ShapeDto>();
	@XmlElement(name = "Edge")
	private List<EdgeDto> edges = new ArrayList<EdgeDto>();

	public Plane toEntity() {

		Plane entity = new Plane();
		entity.setPlaneId(CommonUtil.parseIdFromXmlId(this.getXmlId()));

		// entity.setElement(this.getElement());

		entity.setShapes(ShapeDto.toEntityList(this.getShapes()));
		entity.setEdges(EdgeDto.toEntityList(this.getEdges()));

		return entity;
	}

	public static PlaneDto toDto(final Plane entity, String processXmlId) {

		PlaneDto dto = new PlaneDto();

		dto.setPlaneId(entity.getPlaneId());
		dto.setXmlId(entity.getPlaneId().toString() + "_" + "Plane");

		dto.setElement(processXmlId);

		dto.setShapes(ShapeDto.toDtoList(entity.getShapes()));
		dto.setEdges(EdgeDto.toDtoList(entity.getEdges()));

		return dto;
	}
}
