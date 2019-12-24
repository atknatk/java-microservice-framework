package com.esys.bpm.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.esys.bpm.entity.Process;
import com.esys.bpm.entity.UserTask;
import com.esys.bpm.enums.ProcessComponent;
import com.esys.bpm.utils.CommonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)

@XmlRootElement(name = "userTask")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class UserTaskDto extends TaskDto {

	@XmlTransient
	private ProcessComponent component = ProcessComponent.USER_TASK;

	@XmlTransient
	Long userTaskId;

	@XmlTransient
	List<ParticipantDto> assignees = new ArrayList<ParticipantDto>();
	@XmlTransient
	List<NotificationDetailDto> notificationDetails = new ArrayList<NotificationDetailDto>();

	public UserTask toEntity(Process process) {

		UserTask entity = toEntity();

		if (process != null)
			entity.setProcess(process);

		return entity;
	}

	public UserTask toEntity() {

		UserTask entity = new UserTask();

		entity.setUserTaskId(CommonUtil.parseIdFromXmlId(this.getXmlId()));
		entity.setXmlId(CommonUtil.parseOriginalXmlIdFromXmlId(this.getXmlId()));

		entity.setName(this.getName());
		entity.setDescription(this.getDescription());

		entity.setAssignees(ParticipantDto.toEntityList(this.getAssignees(), null, entity, null, null, null));
		entity.setNotificationDetails(NotificationDetailDto.toEntityList(this.getNotificationDetails(), null, entity));

		return entity;
	}

	public static UserTaskDto toDto(final UserTask entity) {

		UserTaskDto dto = new UserTaskDto();

		dto.setUserTaskId(entity.getUserTaskId());
		dto.setXmlId(entity.getUserTaskId().toString() + "_" + entity.getXmlId());

		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());

		dto.setAssignees(ParticipantDto.toDtoList(entity.getAssignees()));
		dto.setNotificationDetails(NotificationDetailDto.toDtoList(entity.getNotificationDetails()));

		return dto;
	}

	public static List<UserTask> toEntityList(final List<UserTaskDto> dtoList, Process process) {
		List<UserTask> entityList = new ArrayList<UserTask>(0);
		if (dtoList != null) {
			for (UserTaskDto dto : dtoList) {
				UserTask entity = dto.toEntity(process);
				entityList.add(entity);
			}
		}
		return entityList;
	}

	public static List<UserTaskDto> toDtoList(final List<UserTask> entityList) {
		List<UserTaskDto> dtoList = new ArrayList<UserTaskDto>(0);
		if (entityList != null) {
			for (UserTask entity : entityList) {
				UserTaskDto dto = UserTaskDto.toDto(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
}
