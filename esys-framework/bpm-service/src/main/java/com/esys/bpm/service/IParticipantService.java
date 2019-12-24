package com.esys.bpm.service;

import java.util.List;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.dto.ParticipantDto;
import com.esys.framework.core.dto.organization.EmployeeDto;
import com.esys.framework.core.dto.uaa.RoleDto;
import com.esys.framework.core.dto.uaa.UserGroupDto;

@SuppressWarnings("rawtypes")
public interface IParticipantService {

	BpmBaseResult addOrUpdateAssignee(List<ParticipantDto> participants);

	BpmBaseResult<List<EmployeeDto>> getUsers(Long companyId);

	BpmBaseResult<List<UserGroupDto>> getUserGroups(Long companyId);

	BpmBaseResult<List<RoleDto>> getBpmRoles();

	BpmBaseResult addBpmRole(RoleDto role);

	BpmBaseResult updateBpmRole(RoleDto role);

	BpmBaseResult deleteBpmRole(Long roleId);

	BpmBaseResult addUserToBpmRole(Long roleId, Long userId);

	BpmBaseResult removeUserFromBpmRole(Long roleId, Long userId);

	BpmBaseResult validateUser(Long participantId);

	BpmBaseResult validateUserGroup(Long participantId);

	BpmBaseResult validateBpmRole(Long participantId);

	BpmBaseResult validateSystemRole(Long participantId);

	BpmBaseResult validateParticipant(String xmlId, ParticipantDto participant);

	BpmBaseResult validateParticipants(String xmlId, List<ParticipantDto> participants);

	BpmBaseResult<ParticipantDto> saveParticipant(ParticipantDto participant);

}
