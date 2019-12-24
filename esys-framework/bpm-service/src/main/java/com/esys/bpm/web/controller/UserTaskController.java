package com.esys.bpm.web.controller;

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
import com.esys.bpm.dto.TaskDto;
import com.esys.bpm.dto.UserTaskDto;
import com.esys.bpm.service.IUserService;
import com.esys.bpm.utils.CommonUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("api/v1/userTask")
@Api(tags = { "userTask" })
public class UserTaskController {

	@Autowired
	private IUserService userService;

	@ApiOperation(notes = "If process is not saved, task will be just validated but not saved. It is because a process must exist in db in order to make changes. If task exists in db, it will first validate then updates task.", value = "Saves selected User Task information according to drag drop UI", response = BpmBaseResult.class, responseContainer = "UserTaskDto")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns BpmBaseResult containing info, warning, error messages according to operation. Returns saved or validated UserTaskDto object.", response = BpmBaseResult.class) })
	@PostMapping(path = "/saveTask")
	public ResponseEntity<BpmBaseResult> saveTask(
			@ApiParam(value = "User Task Dto object containing entire info", required = true) @RequestBody UserTaskDto input) {

		/*
		 * input = new UserTaskDto(); input.setXmlId("2_1_1");
		 * input.setName("User Task Test");
		 * input.setDescription("User Task Description");
		 * 
		 * ParticipantDto p1 = new ParticipantDto(); p1.setUserId(10L);
		 * p1.setRoleType(RoleType.USER); p1.setExpressionType(ExpressionType.NAME);
		 * input.getAssignees().add(p1);
		 * 
		 * ParticipantDto p2 = new ParticipantDto(); p2.setBpmRoleId(11L);
		 * p2.setRoleType(RoleType.BPMROLE); p2.setExpressionType(ExpressionType.NAME);
		 * input.getAssignees().add(p2);
		 * 
		 * ParticipantDto p3 = new ParticipantDto();
		 * p3.setParticipantName("UserGroupObject.Id");
		 * p3.setRoleType(RoleType.USERGROUP);
		 * p3.setExpressionType(ExpressionType.OBJECT); input.getAssignees().add(p3);
		 * 
		 * NotificationDetailDto detail1 = new NotificationDetailDto();
		 * detail1.setNotificationType(NotificationType.MAIL);
		 * detail1.setSubject("Subject test"); detail1.setBody("Test mail body");
		 * detail1.setFrom("info@cndyazilim.com");
		 * 
		 * List<ParticipantDto> to1List = new ArrayList<ParticipantDto>();
		 * 
		 * ParticipantDto p4 = new ParticipantDto(); p4.setUserId(10L);
		 * p4.setRoleType(RoleType.USER); p4.setExpressionType(ExpressionType.NAME);
		 * to1List.add(p4);
		 * 
		 * ParticipantDto p5 = new ParticipantDto(); p5.setBpmRoleId(11L);
		 * p5.setRoleType(RoleType.BPMROLE); p5.setExpressionType(ExpressionType.NAME);
		 * to1List.add(p5); detail1.setToTargets(to1List);
		 * 
		 * List<ParticipantDto> ccList = new ArrayList<ParticipantDto>(); ParticipantDto
		 * p6 = new ParticipantDto(); p6.setParticipantName("UserGroupObject.Id");
		 * p6.setRoleType(RoleType.USERGROUP);
		 * p6.setExpressionType(ExpressionType.OBJECT); ccList.add(p6);
		 * detail1.setCcTargets(ccList);
		 * 
		 * NotificationDetailDto detail2 = new NotificationDetailDto();
		 * detail2.setNotificationType(NotificationType.SMS);
		 * detail2.setBody("Test SMS body");
		 * 
		 * List<ParticipantDto> to2List = new ArrayList<ParticipantDto>();
		 * ParticipantDto p7 = new ParticipantDto(); p7.setUserId(10L);
		 * p7.setRoleType(RoleType.USER); p7.setExpressionType(ExpressionType.NAME);
		 * to2List.add(p7); detail2.setToTargets(to2List);
		 * 
		 * input.getNotificationDetails().add(detail1);
		 * input.getNotificationDetails().add(detail2);
		 */

		BpmBaseResult output;

		if (CommonUtil.isSavedTask(input.getXmlId())) {
			output = userService.saveTask(input);
		} else {
			output = userService.validateTask(input);
		}

		return ResponseEntity.status(HttpStatus.OK).body(output);
	}

	@ApiOperation(value = "Gets selected User Task detail information")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns BpmBaseResult containing info, warning, error messages according to operation. Returns TaskDto object if task with given id is found.", response = BpmBaseResult.class) })
	@GetMapping(path = "/findById/{id}", produces = "application/json; charset=UTF-8")
	public ResponseEntity<BpmBaseResult<TaskDto>> findById(
			@ApiParam(value = "User Task xmlId", required = true) @PathVariable String id) {
		/*
		 * UserTaskDto userTask = new UserTaskDto(); userTask.setId(id);
		 * userTask.setUserTaskId(77L); userTask.setName("Finans Onayı");
		 * userTask.setDescription("Finans onayı açıklaması");
		 * 
		 * List<ParticipantDto> participants = new ArrayList<ParticipantDto>();
		 * 
		 * ParticipantDto p2 = new ParticipantDto(); p2.setParticipantId(2L);
		 * p2.setParticipantName("HR.Recruitment"); p2.setRoleType(RoleType.BPMROLE);
		 * p2.setExpressionType(ExpressionType.NAME); p2.setUserId(3L);
		 * p2.setBpmRoleId(7L); participants.add(p2);
		 * 
		 * ParticipantDto p3 = new ParticipantDto(); p3.setParticipantId(3L);
		 * p3.setParticipantName("User.Manager"); p3.setRoleType(RoleType.SYSTEMROLE);
		 * p3.setExpressionType(ExpressionType.OBJECT); p3.setUserId(3L);
		 * p3.setSystemRoleId(8L); participants.add(p3);
		 * 
		 * ParticipantDto p4 = new ParticipantDto(); p4.setParticipantId(4L);
		 * p4.setParticipantName("Company.SalesPerson");
		 * p4.setRoleType(RoleType.USERGROUP); p4.setExpressionType(ExpressionType.SQL);
		 * p4.setUserId(3L); p4.setUserGroupId(9L); participants.add(p4);
		 * 
		 * List<NotificationDetailDto> notifications = new
		 * ArrayList<NotificationDetailDto>();
		 * 
		 * NotificationDetailDto n1 = new NotificationDetailDto();
		 * n1.setNotificationDetailId(56L); n1.setTo(Arrays.asList("Furkan Özer"));
		 * n1.setBody("Deneme mesajı"); n1.setNotificationType(NotificationType.SMS);
		 * notifications.add(n1);
		 * 
		 * NotificationDetailDto n2 = new NotificationDetailDto();
		 * n1.setNotificationDetailId(57L); n1.setTo(Arrays.asList("Gökhan Akın"));
		 * n1.setCc(Arrays.asList("Furkan Özer"));
		 * n1.setBcc(Arrays.asList("Selçuk Meral")); n1.setBody(
		 * "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
		 * ); n1.setNotificationType(NotificationType.MAIL); notifications.add(n2);
		 * 
		 * userTask.setAssignees(participants);
		 * userTask.setNotifications(notifications);
		 * 
		 * output.setData(userTask);
		 */

		BpmBaseResult<TaskDto> output = userService.findById(CommonUtil.parseIdFromXmlId(id));
		return ResponseEntity.status(HttpStatus.OK).body(output);
	}

}
