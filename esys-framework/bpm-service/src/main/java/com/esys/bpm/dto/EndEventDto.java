package com.esys.bpm.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.esys.bpm.common.MessageText;
import com.esys.bpm.entity.EndTask;
import com.esys.bpm.entity.Process;
import com.esys.bpm.enums.ProcessComponent;
import com.esys.bpm.utils.CommonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)

@XmlRootElement(name = "endEvent")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class EndEventDto extends EventDto {

	@XmlTransient
	private ProcessComponent component = ProcessComponent.END;

	@XmlTransient
	private Long endTaskId;

	public EndTask toEntity(Process process) {

		EndTask entity = new EndTask();

		entity.setEndTaskId(CommonUtil.parseIdFromXmlId(this.getXmlId()));
		entity.setXmlId(CommonUtil.parseOriginalXmlIdFromXmlId(this.getXmlId()));

		if (this.getName() == null)
			entity.setName(MessageText.END);
		else
			entity.setName(this.getName());
		entity.setDescription(this.getDescription());

		if (process != null)
			entity.setProcess(process);

		return entity;
	}

	public static EndEventDto toDto(final EndTask entity) {

		EndEventDto dto = new EndEventDto();

		dto.setEndTaskId(entity.getEndTaskId());
		dto.setXmlId(entity.getEndTaskId().toString() + "_" + entity.getXmlId());

		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());

		return dto;
	}

	public static List<EndTask> toEntityList(final List<EndEventDto> dtoList, Process process) {
		List<EndTask> entityList = new ArrayList<EndTask>(0);
		if (dtoList != null) {
			for (EndEventDto dto : dtoList) {
				EndTask entity = dto.toEntity(process);
				entityList.add(entity);
			}
		}
		return entityList;
	}

	public static List<EndEventDto> toDtoList(final List<EndTask> entityList) {
		List<EndEventDto> dtoList = new ArrayList<EndEventDto>(0);
		if (entityList != null) {
			for (EndTask entity : entityList) {
				EndEventDto dto = EndEventDto.toDto(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
}
