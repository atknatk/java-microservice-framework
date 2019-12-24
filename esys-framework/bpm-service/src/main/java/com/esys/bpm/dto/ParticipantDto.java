package com.esys.bpm.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.esys.bpm.entity.NotificationDetail;
import com.esys.bpm.entity.Participant;
import com.esys.bpm.entity.Process;
import com.esys.bpm.entity.UserTask;
import com.esys.bpm.enums.ExpressionType;
import com.esys.bpm.enums.RoleType;

import lombok.Data;

@Data
public class ParticipantDto {
	private Long participantId;
	private Long userId;
	private Long userGroupId;
	private Long bpmRoleId;
	private Long systemRoleId;
	private String participantName;

	@Enumerated(EnumType.STRING)
	private RoleType roleType;

	@Enumerated(EnumType.STRING)
	private ExpressionType expressionType;

	public Participant toEntity() {

		Participant entity = new Participant();

		entity.setParticipantId(this.getParticipantId());
		entity.setUserId(this.getUserId());
		entity.setUserGroupId(this.getUserGroupId());
		entity.setBpmRoleId(this.getBpmRoleId());
		entity.setSystemRoleId(this.getSystemRoleId());
		entity.setParticipantName(this.getParticipantName());
		entity.setRoleType(this.getRoleType());
		entity.setExpressionType(this.getExpressionType());

		return entity;
	}

	public Participant toEntity(Process process, UserTask userTask, NotificationDetail toTarget,
			NotificationDetail ccTarget, NotificationDetail bccTarget) {

		Participant entity = toEntity();

		entity.setProcess(process);
		entity.setUserTask(userTask);
		entity.setToTarget(toTarget);
		entity.setCcTarget(ccTarget);
		entity.setBccTarget(bccTarget);

		return entity;
	}

	public static ParticipantDto toDto(final Participant entity) {

		ParticipantDto dto = new ParticipantDto();
		dto.setParticipantId(entity.getParticipantId());
		dto.setUserId(entity.getUserGroupId());
		dto.setUserGroupId(entity.getUserGroupId());
		dto.setBpmRoleId(entity.getBpmRoleId());
		dto.setSystemRoleId(entity.getSystemRoleId());
		dto.setParticipantName(entity.getParticipantName());
		dto.setRoleType(entity.getRoleType());
		dto.setExpressionType(entity.getExpressionType());

		return dto;
	}

	public static List<Participant> toEntityList(final List<ParticipantDto> dtoList, Process process, UserTask userTask,
			NotificationDetail toTarget, NotificationDetail ccTarget, NotificationDetail bccTarget) {
		List<Participant> entityList = new ArrayList<Participant>(0);
		if (dtoList != null) {
			for (ParticipantDto dto : dtoList) {
				Participant entity = dto.toEntity(process, userTask, toTarget, ccTarget, bccTarget);
				entityList.add(entity);
			}
		}
		return entityList;
	}

	public static List<ParticipantDto> toDtoList(final List<Participant> entityList) {
		List<ParticipantDto> dtoList = new ArrayList<ParticipantDto>(0);
		if (entityList != null) {
			for (Participant entity : entityList) {
				ParticipantDto dto = ParticipantDto.toDto(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
}
