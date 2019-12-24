package com.esys.bpm.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.esys.bpm.entity.Process;
import com.esys.bpm.entity.TimerTask;
import com.esys.bpm.enums.ProcessComponent;
import com.esys.bpm.enums.TimerType;
import com.esys.bpm.utils.CommonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)

@XmlRootElement(name = "timerTask")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class TimerTaskDto extends TaskDto {

	@XmlTransient
	private ProcessComponent component = ProcessComponent.TIMER_TASK;

	@XmlTransient
	private Long timerTaskId;

	@XmlTransient
	private TimerType timerType;

	@XmlTransient
	private LocalDateTime specificDate;
	@XmlTransient
	private Long interval;
	@XmlTransient
	private Long schedule;

	public TimerTask toEntity() {

		TimerTask entity = new TimerTask();

		entity.setTimerTaskId(CommonUtil.parseIdFromXmlId(this.getXmlId()));
		entity.setXmlId(CommonUtil.parseOriginalXmlIdFromXmlId(this.getXmlId()));

		entity.setName(this.getName());
		entity.setDescription(this.getDescription());

		entity.setTimerType(this.getTimerType());
		entity.setSpecificDate(this.getSpecificDate());
		entity.setInterval(this.getInterval());
		entity.setSchedule(this.getSchedule());

		return entity;
	}

	public TimerTask toEntity(Process process) {

		TimerTask entity = toEntity();

		if (process != null)
			entity.setProcess(process);

		return entity;
	}

	public static TimerTaskDto toDto(final TimerTask entity) {
		TimerTaskDto dto = new TimerTaskDto();

		dto.setTimerTaskId(entity.getTimerTaskId());
		dto.setXmlId(entity.getTimerTaskId().toString() + "_" + entity.getXmlId());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());

		dto.setTimerType(entity.getTimerType());
		dto.setInterval(entity.getInterval());
		dto.setSchedule(entity.getSchedule());

		return dto;
	}

	public static List<TimerTask> toEntityList(final List<TimerTaskDto> dtoList, Process process) {
		List<TimerTask> entityList = new ArrayList<TimerTask>(0);
		if (dtoList != null) {
			for (TimerTaskDto dto : dtoList) {
				TimerTask entity = dto.toEntity(process);
				entityList.add(entity);
			}
		}
		return entityList;
	}

	public static List<TimerTaskDto> toDtoList(final List<TimerTask> entityList) {
		List<TimerTaskDto> dtoList = new ArrayList<TimerTaskDto>(0);
		if (entityList != null) {
			for (TimerTask entity : entityList) {
				TimerTaskDto dto = TimerTaskDto.toDto(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
}
