package com.esys.bpm.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.esys.bpm.entity.Process;
import com.esys.bpm.entity.ScriptTask;
import com.esys.bpm.enums.ProcessComponent;
import com.esys.bpm.utils.CommonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)

@XmlRootElement(name = "scriptTask")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class ScriptTaskDto extends TaskDto {

	@XmlTransient
	private ProcessComponent component = ProcessComponent.SCRIPT_TASK;

	@XmlTransient
	private Long scriptTaskId;

	@XmlTransient
	private List<ScriptTaskDetailDto> scriptTaskDetails = new ArrayList<ScriptTaskDetailDto>();

	public ScriptTask toEntity() {

		ScriptTask entity = new ScriptTask();

		entity.setScriptTaskId(CommonUtil.parseIdFromXmlId(this.getXmlId()));
		entity.setXmlId(CommonUtil.parseOriginalXmlIdFromXmlId(this.getXmlId()));

		entity.setName(this.getName());
		entity.setDescription(this.getDescription());

		entity.setScriptTaskDetails(ScriptTaskDetailDto.toEntityList(this.getScriptTaskDetails(), entity));

		return entity;
	}

	public ScriptTask toEntity(Process process) {

		ScriptTask entity = toEntity();

		if (process != null)
			entity.setProcess(process);

		return entity;
	}

	public static ScriptTaskDto toDto(final ScriptTask entity) {
		ScriptTaskDto dto = new ScriptTaskDto();

		dto.setScriptTaskId(entity.getScriptTaskId());
		dto.setXmlId(entity.getScriptTaskId().toString() + "_" + entity.getXmlId());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());

		dto.setScriptTaskDetails(ScriptTaskDetailDto.toDtoList(entity.getScriptTaskDetails()));
		return dto;
	}

	public static List<ScriptTask> toEntityList(final List<ScriptTaskDto> dtoList, Process process) {
		List<ScriptTask> entityList = new ArrayList<ScriptTask>(0);
		if (dtoList != null) {
			for (ScriptTaskDto dto : dtoList) {
				ScriptTask entity = dto.toEntity(process);
				entityList.add(entity);
			}
		}
		return entityList;
	}

	public static List<ScriptTaskDto> toDtoList(final List<ScriptTask> entityList) {
		List<ScriptTaskDto> dtoList = new ArrayList<ScriptTaskDto>(0);
		if (entityList != null) {
			for (ScriptTask entity : entityList) {
				ScriptTaskDto dto = ScriptTaskDto.toDto(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
}
