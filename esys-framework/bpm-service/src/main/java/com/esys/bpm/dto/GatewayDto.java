package com.esys.bpm.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.esys.bpm.entity.Gateway;
import com.esys.bpm.entity.Process;
import com.esys.bpm.enums.GatewayType;
import com.esys.bpm.enums.ProcessComponent;
import com.esys.bpm.utils.CommonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class GatewayDto extends BpmBaseDto {

	@XmlTransient
	private ProcessComponent component = ProcessComponent.GATEWAY;

	@XmlTransient
	private GatewayType type;

	@XmlTransient
	private Long gatewayId;

	@XmlElement(name = "incoming")
	private List<String> incomings = new ArrayList<String>();
	@XmlElement(name = "outgoing")
	private List<String> outgoings = new ArrayList<String>();

	public Gateway toEntity() {

		Gateway entity = new Gateway();

		entity.setGatewayId(CommonUtil.parseIdFromXmlId(this.getXmlId()));
		entity.setXmlId(CommonUtil.parseOriginalXmlIdFromXmlId(this.getXmlId()));

		entity.setName(this.getName());
		entity.setDescription(this.getDescription());
		entity.setType(this.getType());

		return entity;
	}

	public Gateway toEntity(Process process) {

		Gateway entity = toEntity();

		entity.setGatewayId(CommonUtil.parseIdFromXmlId(this.getXmlId()));
		entity.setXmlId(CommonUtil.parseOriginalXmlIdFromXmlId(this.getXmlId()));

		entity.setName(this.getName());
		entity.setDescription(this.getDescription());
		entity.setType(type);

		if (process != null)
			entity.setProcess(process);

		return entity;
	}

	public static GatewayDto toDto(final Gateway entity) {
		GatewayDto dto = new GatewayDto();

		dto.setGatewayId(entity.getGatewayId());
		dto.setXmlId(entity.getGatewayId().toString() + "_" + entity.getXmlId());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setType(entity.getType());

		return dto;
	}

	public static List<Gateway> toEntityList(final List<GatewayDto> dtoList, Process process) {
		List<Gateway> entityList = new ArrayList<Gateway>(0);
		if (dtoList != null) {
			for (GatewayDto dto : dtoList) {
				Gateway entity = dto.toEntity(process);
				entityList.add(entity);
			}
		}
		return entityList;
	}

	/*
	 * public static List<GatewayDto> toDtoList(final List<Gateway> entityList) {
	 * List<GatewayDto> dtoList = new ArrayList<GatewayDto>(0); if (entityList !=
	 * null) { for (Gateway entity : entityList) { GatewayDto dto =
	 * GatewayDto.toDto(entity); dtoList.add(dto); } } return dtoList; }
	 */
}
