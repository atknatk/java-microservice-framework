package com.esys.bpm.dto;

import java.util.ArrayList;
import java.util.List;

import com.esys.bpm.entity.ScriptTask;
import com.esys.bpm.entity.ScriptTaskDetail;

import lombok.Data;

@Data
public class ScriptTaskDetailDto {

	private Long scriptTaskDetailId;

	private String variableName;
	private String newValue;

	public ScriptTaskDetail toEntity(ScriptTask scriptTask) {

		ScriptTaskDetail entity = new ScriptTaskDetail();

		entity.setScriptTaskDetailId(this.getScriptTaskDetailId());
		entity.setVariableName(this.getVariableName());
		entity.setNewValue(this.getNewValue());

		if (scriptTask != null)
			entity.setScriptTask(scriptTask);

		return entity;
	}

	public static ScriptTaskDetailDto toDto(final ScriptTaskDetail entity) {
		ScriptTaskDetailDto dto = new ScriptTaskDetailDto();

		dto.setScriptTaskDetailId(entity.getScriptTaskDetailId());
		dto.setVariableName(entity.getVariableName());
		dto.setNewValue(entity.getNewValue());

		return dto;
	}

	public static List<ScriptTaskDetail> toEntityList(final List<ScriptTaskDetailDto> dtoList, ScriptTask scriptTask) {
		List<ScriptTaskDetail> entityList = new ArrayList<ScriptTaskDetail>(0);
		if (dtoList != null) {
			for (ScriptTaskDetailDto dto : dtoList) {
				ScriptTaskDetail entity = dto.toEntity(scriptTask);
				entityList.add(entity);
			}
		}
		return entityList;
	}

	public static List<ScriptTaskDetailDto> toDtoList(final List<ScriptTaskDetail> entityList) {
		List<ScriptTaskDetailDto> dtoList = new ArrayList<ScriptTaskDetailDto>(0);
		if (entityList != null) {
			for (ScriptTaskDetail entity : entityList) {
				ScriptTaskDetailDto dto = ScriptTaskDetailDto.toDto(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
}
