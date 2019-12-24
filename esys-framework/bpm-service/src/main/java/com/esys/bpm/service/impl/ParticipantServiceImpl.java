package com.esys.bpm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.common.MessageText;
import com.esys.bpm.dto.ParticipantDto;
import com.esys.bpm.entity.Participant;
import com.esys.bpm.enums.ExpressionType;
import com.esys.bpm.enums.RoleType;
import com.esys.bpm.repository.ParticipantRepository;
import com.esys.bpm.service.IParticipantService;
import com.esys.bpm.utils.StringUtil;
import com.esys.framework.core.dto.organization.EmployeeDto;
import com.esys.framework.core.dto.uaa.RoleDto;
import com.esys.framework.core.dto.uaa.UserGroupDto;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service("participantService")
public class ParticipantServiceImpl implements IParticipantService {

	@Autowired
	private ParticipantRepository participantRepository;

	@Override
	public BpmBaseResult validateParticipant(String xmlId, ParticipantDto participant) {
		BpmBaseResult<Object> result = new BpmBaseResult<>();

		if (participant.getRoleType() == null)
			result.addErrorMessage(xmlId, MessageText.EMPTY_ROLE_TYPE);
		else if (participant.getExpressionType().equals(ExpressionType.NAME)
				&& participant.getRoleType().equals(RoleType.USER))
			result.addMessages(validateUser(participant.getUserId()).getMessages());
		else if (participant.getExpressionType().equals(ExpressionType.NAME)
				&& participant.getRoleType().equals(RoleType.USERGROUP))
			result.addMessages(validateUserGroup(participant.getUserGroupId()).getMessages());
		else if (participant.getExpressionType().equals(ExpressionType.NAME)
				&& participant.getRoleType().equals(RoleType.BPMROLE))
			result.addMessages(validateBpmRole(participant.getBpmRoleId()).getMessages());
		else if (participant.getExpressionType().equals(ExpressionType.NAME)
				&& participant.getRoleType().equals(RoleType.SYSTEMROLE))
			result.addMessages(validateSystemRole(participant.getSystemRoleId()).getMessages());
		else if ((participant.getExpressionType().equals(ExpressionType.OBJECT)
				|| participant.getExpressionType().equals(ExpressionType.SQL))
				&& StringUtil.isNullOrEmptyOrWhitespace(participant.getParticipantName()))
			result.addErrorMessage(xmlId, MessageText.EMPTY);

		// TODO Sql kontrollerine karar verilecek
		// TODO Object kontrollerine karar verilecek

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BpmBaseResult validateParticipants(String xmlId, List<ParticipantDto> participants) {
		BpmBaseResult result = new BpmBaseResult<>();

		for (ParticipantDto participant : participants) {
			result.addMessages(validateParticipant(xmlId, participant).getMessages());
		}

		return result;
	}

	@Override
	public BpmBaseResult<ParticipantDto> saveParticipant(ParticipantDto participant) {
		ParticipantDto dto = participant;

		BpmBaseResult<ParticipantDto> result = validateParticipant(null, dto);
		if (result.hasErrors())
			return result;
		else {
			try {
				Participant newEntity = dto.toEntity();
				Participant dbEntity;

				if (newEntity.getParticipantId() == null) {
					dbEntity = participantRepository.save(newEntity);
				} else {
					dbEntity = participantRepository.findById(newEntity.getParticipantId()).get();

					dbEntity.setUserId(newEntity.getUserId());
					dbEntity.setUserGroupId(newEntity.getUserGroupId());
					dbEntity.setBpmRoleId(newEntity.getBpmRoleId());
					dbEntity.setSystemRoleId(newEntity.getSystemRoleId());
					dbEntity.setParticipantName(newEntity.getParticipantName());
					dbEntity.setRoleType(newEntity.getRoleType());
					dbEntity.setExpressionType(newEntity.getExpressionType());

					// TODO değişenleri loglama
				}

				dbEntity = participantRepository.save(dbEntity);
				result.setData(ParticipantDto.toDto(dbEntity));

			} catch (Exception ex) {
				result.addErrorMessage(ex.getMessage());
			}
		}
		return result;
	}

	@Override
	public BpmBaseResult addOrUpdateAssignee(List<ParticipantDto> participants) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BpmBaseResult<List<EmployeeDto>> getUsers(Long companyId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BpmBaseResult<List<UserGroupDto>> getUserGroups(Long companyId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BpmBaseResult<List<RoleDto>> getBpmRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BpmBaseResult addBpmRole(RoleDto role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BpmBaseResult updateBpmRole(RoleDto role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BpmBaseResult deleteBpmRole(Long roleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BpmBaseResult addUserToBpmRole(Long roleId, Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BpmBaseResult removeUserFromBpmRole(Long roleId, Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BpmBaseResult validateUser(Long participantId) {
		// TODO UAA integration
		return new BpmBaseResult<>();
	}

	@Override
	public BpmBaseResult validateUserGroup(Long participantId) {
		// TODO UAA integration
		return new BpmBaseResult<>();
	}

	@Override
	public BpmBaseResult validateBpmRole(Long participantId) {
		// TODO UAA integration
		return new BpmBaseResult<>();
	}

	@Override
	public BpmBaseResult validateSystemRole(Long participantId) {
		// TODO UAA integration
		return new BpmBaseResult<>();
	}

}
