package com.esys.bpm.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.esys.bpm.entity.ProcessLog;

import lombok.Data;

@Data
public class ProcessLogDto {

	private Long processLogId;
	private String explanation;
	private Long userId;
	private LocalDateTime logDate;

	public ProcessLog toEntity() {

		ProcessLog entity = new ProcessLog();

		entity.setProcessLogId(this.getProcessLogId());
		entity.setExplanation(this.getExplanation());
		entity.setUserId(this.getUserId());
		entity.setLogDate(this.getLogDate());

		return entity;
	}

	public static ProcessLogDto toDto(final ProcessLog entity) {

		ProcessLogDto dto = new ProcessLogDto();
		dto.setProcessLogId(entity.getProcessLogId());
		dto.setExplanation(entity.getExplanation());
		dto.setUserId(entity.getUserId());
		dto.setLogDate(entity.getLogDate());

		return dto;
	}

	public static List<ProcessLog> toEntityList(final List<ProcessLogDto> dtoList) {
		List<ProcessLog> entityList = new ArrayList<ProcessLog>(0);
		if (dtoList != null) {
			for (ProcessLogDto dto : dtoList) {
				ProcessLog entity = dto.toEntity();
				entityList.add(entity);
			}
		}
		return entityList;
	}

	public static List<ProcessLogDto> toDtoList(final List<ProcessLog> entityList) {
		List<ProcessLogDto> dtoList = new ArrayList<ProcessLogDto>(0);
		if (entityList != null) {
			for (ProcessLog entity : entityList) {
				ProcessLogDto dto = ProcessLogDto.toDto(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}

}
