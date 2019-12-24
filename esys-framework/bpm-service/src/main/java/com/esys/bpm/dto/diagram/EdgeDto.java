package com.esys.bpm.dto.diagram;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.esys.bpm.entity.diagram.Edge;
import com.esys.bpm.utils.CommonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)

@XmlRootElement(name = "Edge")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class EdgeDto {

	@XmlAttribute(name = "id")
	private String xmlId;

	@XmlTransient
	private Long edgeId;

	@XmlAttribute(name = "bpmnElement")
	private String elementXmlId;
	@XmlTransient
	private Long elementId;

	@XmlElement(name = "Label")
	private LabelDto label;

	@XmlElement(name = "waypoint")
	private List<WaypointDto> waypoints;

	public Edge toEntity() {

		Edge entity = new Edge();

		entity.setEdgeId(CommonUtil.parseIdFromXmlId(this.getXmlId()));
		entity.setXmlId(CommonUtil.parseOriginalXmlIdFromXmlId(this.getXmlId()));

		entity.setElementId(CommonUtil.parseIdFromXmlId(this.getElementXmlId()));
		entity.setElementXmlId(CommonUtil.parseOriginalXmlIdFromXmlId(this.getElementXmlId()));

		entity.setWaypoints(WaypointDto.toEntityList(this.getWaypoints(), entity));
		if (this.getLabel() != null)
			entity.setLabel(this.getLabel().toEntity());

		return entity;
	}

	public static EdgeDto toDto(final Edge entity) {

		EdgeDto dto = new EdgeDto();

		dto.setEdgeId(entity.getEdgeId());
		dto.setXmlId(entity.getEdgeId().toString() + "_" + entity.getXmlId());

		dto.setElementId(entity.getElementId());
		dto.setElementXmlId(entity.getElementId().toString() + "_" + entity.getElementXmlId());

		dto.setWaypoints(WaypointDto.toDtoList(entity.getWaypoints()));

		return dto;
	}

	public static List<Edge> toEntityList(final List<EdgeDto> dtoList) {
		List<Edge> entityList = new ArrayList<Edge>(0);
		if (dtoList != null) {
			for (EdgeDto dto : dtoList) {
				Edge entity = dto.toEntity();
				entityList.add(entity);
			}
		}
		return entityList;
	}

	public static List<EdgeDto> toDtoList(final List<Edge> entityList) {
		List<EdgeDto> dtoList = new ArrayList<EdgeDto>(0);
		if (entityList != null) {
			for (Edge entity : entityList) {
				EdgeDto dto = EdgeDto.toDto(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}

}
