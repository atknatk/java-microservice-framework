package com.esys.bpm.dto.diagram;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.esys.bpm.entity.diagram.Edge;
import com.esys.bpm.entity.diagram.Waypoint;
import com.esys.bpm.utils.CommonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)

@XmlRootElement(name = "waypoint")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class WaypointDto {

	@XmlAttribute
	private String id;
	@XmlAttribute
	private String x;
	@XmlAttribute
	private String y;

	public Waypoint toEntity(Edge edge) {

		Waypoint entity = new Waypoint();

		entity.setWaypointId(CommonUtil.parseLongElseNull(this.getId()));
		entity.setX(this.getX());
		entity.setY(this.getY());

		entity.setEdge(edge);

		return entity;
	}

	public static WaypointDto toDto(final Waypoint entity) {

		WaypointDto dto = new WaypointDto();
		dto.setId(entity.getWaypointId().toString());
		dto.setX(entity.getX());
		dto.setY(entity.getY());

		return dto;
	}

	public static List<Waypoint> toEntityList(final List<WaypointDto> dtoList, Edge edge) {
		List<Waypoint> entityList = new ArrayList<Waypoint>(0);
		if (dtoList != null) {
			for (WaypointDto dto : dtoList) {
				Waypoint entity = dto.toEntity(edge);
				entityList.add(entity);
			}
		}
		return entityList;
	}

	public static List<WaypointDto> toDtoList(final List<Waypoint> entityList) {
		List<WaypointDto> dtoList = new ArrayList<WaypointDto>(0);
		if (entityList != null) {
			for (Waypoint entity : entityList) {
				WaypointDto dto = WaypointDto.toDto(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
}
