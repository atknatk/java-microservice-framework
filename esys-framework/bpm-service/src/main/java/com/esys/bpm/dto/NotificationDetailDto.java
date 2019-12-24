package com.esys.bpm.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.esys.bpm.entity.NotificationDetail;
import com.esys.bpm.entity.NotificationTask;
import com.esys.bpm.entity.UserTask;
import com.esys.bpm.enums.NotificationType;

import lombok.Data;

@Data
public class NotificationDetailDto {

	private Long notificationDetailId;

	private String from;
	private String subject;
	private String body;

	private List<ParticipantDto> toTargets;
	private List<ParticipantDto> bccTargets;
	private List<ParticipantDto> ccTargets;

	@Enumerated(EnumType.STRING)
	private NotificationType notificationType;

	public NotificationDetail toEntity(NotificationTask notificationTask, UserTask userTask) {

		NotificationDetail entity = new NotificationDetail();
		entity.setNotificationDetailId(this.getNotificationDetailId());
		entity.setFromSource(this.getFrom());

		entity.setBody(this.getBody());
		entity.setSubject(this.getSubject());

		entity.setToTargets(ParticipantDto.toEntityList(this.getToTargets(), null, null, entity, null, null));
		entity.setCcTargets(ParticipantDto.toEntityList(this.getCcTargets(), null, null, null, entity, null));
		entity.setBccTargets(ParticipantDto.toEntityList(this.getBccTargets(), null, null, null, null, entity));

		entity.setNotificationType(this.getNotificationType());

		entity.setNotificationTask(notificationTask);
		entity.setUserTask(userTask);

		return entity;
	}

	public static NotificationDetailDto toDto(final NotificationDetail entity) {

		NotificationDetailDto dto = new NotificationDetailDto();
		dto.setNotificationDetailId(entity.getNotificationDetailId());
		dto.setFrom(entity.getFromSource());

		dto.setToTargets(ParticipantDto.toDtoList(entity.getToTargets()));
		dto.setCcTargets(ParticipantDto.toDtoList(entity.getCcTargets()));
		dto.setBccTargets(ParticipantDto.toDtoList(entity.getBccTargets()));
		/*
		 * dto.setTo(Arrays.asList(entity.getToTarget().split(";"))); if
		 * (entity.getBcc() != null)
		 * dto.setBcc(Arrays.asList(entity.getBcc().split(";"))); if (entity.getCc() !=
		 * null) dto.setCc(Arrays.asList(entity.getCc().split(";")));
		 */
		dto.setBody(entity.getBody());
		dto.setSubject(entity.getSubject());
		dto.setNotificationType(entity.getNotificationType());

		return dto;
	}

	public static List<NotificationDetail> toEntityList(final List<NotificationDetailDto> dtoList,
			NotificationTask notificationtask, UserTask userTask) {
		List<NotificationDetail> entityList = new ArrayList<NotificationDetail>(0);
		if (dtoList != null) {
			for (NotificationDetailDto dto : dtoList) {
				NotificationDetail entity = dto.toEntity(notificationtask, userTask);
				entityList.add(entity);
			}
		}
		return entityList;
	}

	public static List<NotificationDetailDto> toDtoList(final List<NotificationDetail> entityList) {
		List<NotificationDetailDto> dtoList = new ArrayList<NotificationDetailDto>(0);
		if (entityList != null) {
			for (NotificationDetail entity : entityList) {
				NotificationDetailDto dto = NotificationDetailDto.toDto(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
}
