package com.esys.bpm.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.esys.bpm.entity.NotificationTask;
import com.esys.bpm.entity.Process;
import com.esys.bpm.enums.ProcessComponent;
import com.esys.bpm.utils.CommonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)

@XmlRootElement(name = "notificationTask")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class NotificationTaskDto extends TaskDto {

	@XmlTransient
	private ProcessComponent component = ProcessComponent.NOTIFICATION_TASK;

	@XmlTransient
	private Long notificationTaskId;

	@XmlTransient
	private List<NotificationDetailDto> notificationDetails = new ArrayList<NotificationDetailDto>();

	public NotificationTask toEntity() {

		NotificationTask entity = new NotificationTask();

		entity.setNotificationTaskId(CommonUtil.parseIdFromXmlId(this.getXmlId()));
		entity.setXmlId(CommonUtil.parseOriginalXmlIdFromXmlId(this.getXmlId()));

		entity.setName(this.getName());
		entity.setDescription(this.getDescription());

		entity.setNotificationDetails(NotificationDetailDto.toEntityList(this.getNotificationDetails(), entity, null));

		return entity;
	}

	public NotificationTask toEntity(Process process) {

		NotificationTask entity = toEntity();

		if (process != null)
			entity.setProcess(process);

		return entity;
	}

	public static NotificationTaskDto toDto(final NotificationTask entity) {
		NotificationTaskDto dto = new NotificationTaskDto();

		dto.setNotificationTaskId(entity.getNotificationTaskId());
		dto.setXmlId(entity.getNotificationTaskId().toString() + "_" + entity.getXmlId());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());

		dto.setNotificationDetails(NotificationDetailDto.toDtoList(entity.getNotificationDetails()));
		return dto;
	}

	public static List<NotificationTask> toEntityList(final List<NotificationTaskDto> dtoList, Process process) {
		List<NotificationTask> entityList = new ArrayList<NotificationTask>(0);
		if (dtoList != null) {
			for (NotificationTaskDto dto : dtoList) {
				NotificationTask entity = dto.toEntity(process);
				entityList.add(entity);
			}
		}
		return entityList;
	}

	public static List<NotificationTaskDto> toDtoList(final List<NotificationTask> entityList) {
		List<NotificationTaskDto> dtoList = new ArrayList<NotificationTaskDto>(0);
		if (entityList != null) {
			for (NotificationTask entity : entityList) {
				NotificationTaskDto dto = NotificationTaskDto.toDto(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
}
