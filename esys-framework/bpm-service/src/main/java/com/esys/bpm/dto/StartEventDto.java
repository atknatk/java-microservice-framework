package com.esys.bpm.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.esys.bpm.common.MessageText;
import com.esys.bpm.entity.Process;
import com.esys.bpm.entity.StartTask;
import com.esys.bpm.enums.ProcessComponent;
import com.esys.bpm.utils.CommonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)

@XmlRootElement(name = "startEvent")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class StartEventDto extends EventDto {

	@XmlTransient
	private ProcessComponent component = ProcessComponent.START;

	@XmlTransient
	private Long startTaskId;

	public StartTask toEntity(Process process) {

		StartTask entity = new StartTask();

		entity.setStartTaskId(CommonUtil.parseIdFromXmlId(this.getXmlId()));
		entity.setXmlId(CommonUtil.parseOriginalXmlIdFromXmlId(this.getXmlId()));

		if (this.getName() == null)
			entity.setName(MessageText.START);
		else
			entity.setName(this.getName());
		entity.setDescription(this.getDescription());

		if (process != null)
			entity.setProcess(process);

		return entity;
	}

	public static StartEventDto toDto(final StartTask entity) {

		StartEventDto dto = new StartEventDto();
		dto.setXmlId(entity.getStartTaskId().toString() + "_" + entity.getXmlId());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());

		return dto;
	}

	public static List<StartTask> toEntityList(final List<StartEventDto> dtoList, Process process) {
		List<StartTask> entityList = new ArrayList<StartTask>(0);
		if (entityList != null) {
			for (StartEventDto dto : dtoList) {
				StartTask entity = dto.toEntity(process);
				entityList.add(entity);
			}
		}
		return entityList;
	}

	public static List<StartEventDto> toDtoList(final List<StartTask> entityList) {
		List<StartEventDto> dtoList = new ArrayList<StartEventDto>(0);
		if (entityList != null) {
			for (StartTask entity : entityList) {
				StartEventDto dto = StartEventDto.toDto(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
}
