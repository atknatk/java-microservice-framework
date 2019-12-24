package com.esys.bpm.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.dto.ParticipantDto;
import com.esys.bpm.service.IParticipantService;
import com.esys.bpm.web.controller.input.RoleUserInput;
import com.esys.framework.core.dto.organization.EmployeeDto;
import com.esys.framework.core.dto.uaa.RoleDto;
import com.esys.framework.core.dto.uaa.UserGroupDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("api/v1/participant")
@Api(tags = { "participant" })
public class ParticipantController {

	@Autowired
	private IParticipantService participantService;

	@ApiOperation(value = "Saves Participant information whether it is user, user group, bpm authority or system authority", response = BpmBaseResult.class, responseContainer = "ParticipantDto")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns BpmBaseResult containing info, warning, error messages according to operation. Returns saved ParticipantDto object.", response = BpmBaseResult.class) })
	@PostMapping(path = "/saveParticipant")
	public ResponseEntity<BpmBaseResult> saveParticipant(
			@ApiParam(value = "Participant Dto object contanining entire info", required = true) @RequestBody ParticipantDto input) {

		/*
		 * input = new ParticipantDto(); input.setUserId(10L);
		 * input.setRoleType(RoleType.USER);
		 * input.setExpressionType(ExpressionType.NAME);
		 */

		/*
		 * input = new ParticipantDto(); input.setBpmRoleId(11L);
		 * input.setRoleType(RoleType.BPMROLE);
		 * input.setExpressionType(ExpressionType.NAME);
		 */

		/*
		 * input = new ParticipantDto(); input.setParticipantName("UserGroupObject.Id");
		 * input.setRoleType(RoleType.USERGROUP);
		 * input.setExpressionType(ExpressionType.OBJECT);
		 */

		BpmBaseResult output = participantService.saveParticipant(input);

		return ResponseEntity.status(HttpStatus.OK).body(output);
	}

	/*
	 * @GetMapping(path = "/getUserTaskById/{userTaskId}") public List<UserTaskDto>
	 * getUserTaskById(@PathVariable Long userTaskId) { List<UserTaskDto>
	 * userTaskList = new ArrayList<UserTaskDto>();
	 *
	 * UserTaskDto user1 = new UserTaskDto(); user1.setId(userTaskId);
	 * user1.setId(userTaskId);
	 *
	 * // List<EmployeeDto> output = participantService.getUsers(companyId); return
	 * output; }
	 */
	@ApiOperation(value = "Gets company active users")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns Employee Dto list including id and names", response = EmployeeDto.class) })
	@GetMapping(path = "/getUsers/{companyId}", produces = "application/json; charset=UTF-8")
	public BpmBaseResult<List<EmployeeDto>> getUsers(
			@ApiParam(value = "Company id that Processes belong to", required = true) @PathVariable String companyId) {

		BpmBaseResult<List<EmployeeDto>> result = new BpmBaseResult<List<EmployeeDto>>();

		List<EmployeeDto> output = new ArrayList<EmployeeDto>();
		EmployeeDto e1 = new EmployeeDto();
		e1.setName("Furkan");
		e1.setSurname("Özer");
		output.add(e1);

		EmployeeDto e2 = new EmployeeDto();
		e2.setName("Gökhan");
		e2.setSurname("Akın");
		output.add(e2);

		result.setData(output);
		result.addWarningMessage("Dummy data is used because lack of uaa service integration!");

		return result;
	}

	@ApiOperation(value = "Gets user groups according to active company")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns User Group Dto list including id and names", response = UserGroupDto.class) })
	@GetMapping(path = "/getUserGroups/{companyId}", produces = "application/json; charset=UTF-8")
	public BpmBaseResult<List<UserGroupDto>> getUserGroups(
			@ApiParam(value = "Company id that Processes belong to", required = true) @PathVariable String companyId) {
		// List<UserGroupDto> output = participantService.getUserGroups(companyId);

		BpmBaseResult<List<UserGroupDto>> result = new BpmBaseResult<List<UserGroupDto>>();

		List<UserGroupDto> output = new ArrayList<UserGroupDto>();
		UserGroupDto ug1 = new UserGroupDto();
		ug1.setId(1L);
		ug1.setName("User Group 1");
		output.add(ug1);

		UserGroupDto ug2 = new UserGroupDto();
		ug2.setId(2L);
		ug2.setName("User Group 2");
		output.add(ug2);

		UserGroupDto ug3 = new UserGroupDto();
		ug3.setId(3L);
		ug3.setName("User Group 3");
		output.add(ug3);

		result.setData(output);
		result.addWarningMessage("Dummy data is used because lack of uaa service integration!");
		return result;
	}

	@ApiOperation(value = "Gets BPM roles which are created in current and previously designed processes. It only fetches active company BPM roles")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns Role Dto list including id and names", response = RoleDto.class) })
	@GetMapping(path = "/getBpmRoles/{companyId}", produces = "application/json; charset=UTF-8")
	public BpmBaseResult<List<RoleDto>> getBpmRoles(
			@ApiParam(value = "Company id that Processes belong to", required = true) @PathVariable String companyId) {
		// List<RoleDto> output = participantService.getBpmRoles();

		BpmBaseResult<List<RoleDto>> result = new BpmBaseResult<List<RoleDto>>();

		List<RoleDto> output = new ArrayList<RoleDto>();
		RoleDto r1 = new RoleDto();
		r1.setId(11L);
		r1.setName("TestProcess.FinanceApprove");
		output.add(r1);

		RoleDto r2 = new RoleDto();
		r2.setId(22L);
		r2.setName("TestProcess.SalesApprove");
		output.add(r2);

		RoleDto r3 = new RoleDto();
		r3.setId(33L);
		r3.setName("TestProcess.ManagerApprove");
		output.add(r3);

		result.setData(output);
		result.addWarningMessage("Dummy data is used because lack of uaa service integration!");
		return result;
	}

	@ApiOperation(value = "Creates a new BPM authority if authority does not exist", response = BpmBaseResult.class, responseContainer = "RoleDto")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns BpmBaseResult containing info, warning, error messages according to operation. Returns saved RoleDto object.", response = BpmBaseResult.class) })
	@PostMapping(path = "/addBpmRole")
	public ResponseEntity<BpmBaseResult> addBpmRole(
			@ApiParam(value = "BPM authority object containing entire info", required = true) @RequestBody RoleDto input) {
		BpmBaseResult output = participantService.addBpmRole(input);
		return ResponseEntity.status(HttpStatus.OK).body(output);
	}

	@ApiOperation(value = "Updates existing BPM authority", response = BpmBaseResult.class, responseContainer = "RoleDto")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns BpmBaseResult containing info, warning, error messages according to operation. Returns saved RoleDto object.", response = BpmBaseResult.class) })
	@PostMapping(path = "/updateBpmRole")
	public ResponseEntity<BpmBaseResult> updateBpmRole(
			@ApiParam(value = "BPM authority object that is to be updated", required = true) @RequestBody RoleDto input) {
		BpmBaseResult output = participantService.updateBpmRole(input);
		return ResponseEntity.status(HttpStatus.OK).body(output);
	}

	@ApiOperation(value = "Deletes existing BPM authority ", response = BpmBaseResult.class, responseContainer = "RoleDto")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns BpmBaseResult containing info, warning, error messages according to operation. Returns deleted RoleDto object.", response = BpmBaseResult.class) })
	@PostMapping(path = "/deleteBpmRole")
	public ResponseEntity<BpmBaseResult> deleteBpmRole(
			@ApiParam(value = "BPM authority object id that is to be deleted", required = true) @RequestBody Long input) {
		BpmBaseResult output = participantService.deleteBpmRole(input);
		return ResponseEntity.status(HttpStatus.OK).body(output);
	}

	@ApiOperation(value = "Adds user to existing Bpm authority", response = BpmBaseResult.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns BpmBaseResult containing info, warning, error messages according to operation. ", response = BpmBaseResult.class) })
	@PostMapping(path = "/addUserToBpmRole")
	public ResponseEntity<BpmBaseResult> addUserToBpmRole(
			@ApiParam(value = "Role User Input object containing authority id and user id", required = true) @RequestBody RoleUserInput input) {
		BpmBaseResult output = participantService.addUserToBpmRole(input.getRoleId(), input.getUserId());
		return ResponseEntity.status(HttpStatus.OK).body(output);
	}

	@ApiOperation(value = "Removes user from existing Bpm authority", response = BpmBaseResult.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns BpmBaseResult containing info, warning, error messages according to operation. ", response = BpmBaseResult.class) })
	@PostMapping(path = "/removeUserFromBpmRole")
	public ResponseEntity<BpmBaseResult> removeUserFromBpmRole(
			@ApiParam(value = "BPM authority object containing authority id and user id.", required = true) @RequestBody RoleUserInput input) {
		BpmBaseResult output = participantService.removeUserFromBpmRole(input.getRoleId(), input.getUserId());
		return ResponseEntity.status(HttpStatus.OK).body(output);
	}

	/*
	 * @SuppressWarnings("rawtypes")
	 *
	 * @PostMapping(path = "/validateParticipant") public
	 * ResponseEntity<BpmBaseResult> validateParticipant(@RequestBody ParticipantDto
	 * input) { BpmBaseResult output =
	 * participantService.validateParticipant(input); return
	 * ResponseEntity.status(HttpStatus.OK).body(output); }
	 *
	 * @SuppressWarnings("rawtypes")
	 *
	 * @PostMapping(path = "/validateParticipants") public
	 * ResponseEntity<BpmBaseResult> validateParticipants(@RequestBody
	 * List<ParticipantDto> input) { BpmBaseResult output =
	 * participantService.validateParticipants(input); return
	 * ResponseEntity.status(HttpStatus.OK).body(output); }
	 */
}
