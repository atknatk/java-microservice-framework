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
import com.esys.bpm.dto.NotificationTaskDto;
import com.esys.bpm.dto.TaskDto;
import com.esys.bpm.service.INotificationService;
import com.esys.bpm.utils.CommonUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("api/v1/notificationTask")
@Api(tags = { "notificationTask" })
public class NotificationTaskController {

	@Autowired
	private INotificationService notificationService;

	@ApiOperation(notes = "If process is not saved, task will be just validated but not saved. It is because a process must exist in db in order to make changes. If task exists in db, it will first validate then updates task.", value = "Saves selected Notification Task information according to drag drop UI", response = BpmBaseResult.class, responseContainer = "NotificationTaskDto")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns BpmBaseResult containing info, warning, error messages according to operation. Returns saved or validated NotificationTaskDto object.", response = BpmBaseResult.class) })
	@PostMapping(path = "/saveTask")
	public ResponseEntity<BpmBaseResult> saveTask(
			@ApiParam(value = "Notification Task Dto object containing entire info", required = true) @RequestBody NotificationTaskDto input) {

		/*
		 * input = new NotificationTaskDto(); input.setXmlId("1_1_1");
		 * input.setName("Notification Test");
		 * input.setDescription("Notification Description");
		 */

		/*
		 * NotificationDetailDto detail1 = new NotificationDetailDto();
		 * detail1.setNotificationType(NotificationType.MAIL);
		 * detail1.setSubject("Subject test"); detail1.setBody("Test mail body");
		 * detail1.setFrom("info@cndyazilim.com");
		 * 
		 * List<ParticipantDto> to1List = new ArrayList<ParticipantDto>();
		 * 
		 * ParticipantDto p1 = new ParticipantDto(); p1.setUserId(10L);
		 * p1.setRoleType(RoleType.USER); p1.setExpressionType(ExpressionType.NAME);
		 * to1List.add(p1);
		 * 
		 * ParticipantDto p2 = new ParticipantDto(); p2.setBpmRoleId(11L);
		 * p2.setRoleType(RoleType.BPMROLE); p2.setExpressionType(ExpressionType.NAME);
		 * to1List.add(p2); detail1.setToTargets(to1List);
		 * 
		 * List<ParticipantDto> ccList = new ArrayList<ParticipantDto>(); ParticipantDto
		 * p3 = new ParticipantDto(); p3.setParticipantName("UserGroupObject.Id");
		 * p3.setRoleType(RoleType.USERGROUP);
		 * p3.setExpressionType(ExpressionType.OBJECT); ccList.add(p3);
		 * detail1.setCcTargets(ccList);
		 * 
		 * NotificationDetailDto detail2 = new NotificationDetailDto();
		 * detail2.setNotificationType(NotificationType.SMS);
		 * detail2.setBody("Test SMS body");
		 * 
		 * List<ParticipantDto> to2List = new ArrayList<ParticipantDto>();
		 * ParticipantDto p4 = new ParticipantDto(); p4.setUserId(10L);
		 * p4.setRoleType(RoleType.USER); p4.setExpressionType(ExpressionType.NAME);
		 * to2List.add(p4); detail2.setToTargets(to2List);
		 * 
		 * input.getNotificationDetails().add(detail1);
		 * input.getNotificationDetails().add(detail2);
		 */

		BpmBaseResult output;

		if (CommonUtil.isSavedTask(input.getXmlId())) {
			output = notificationService.saveTask(input);
		} else {
			output = notificationService.validateTask(input);
		}

		return ResponseEntity.status(HttpStatus.OK).body(output);
	}

	@ApiOperation(value = "Gets selected Notification Task detail information")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns BpmBaseResult containing info, warning, error messages according to operation. Returns TaskDto object if task with given id is found.", response = BpmBaseResult.class) })
	@GetMapping(path = "/findById/{id}", produces = "application/json; charset=UTF-8")
	public ResponseEntity<BpmBaseResult<TaskDto>> findById(
			@ApiParam(value = "Notification Task xmlId", required = true) @PathVariable String id) {
		BpmBaseResult<TaskDto> output = notificationService.findById(CommonUtil.parseIdFromXmlId(id));

		return ResponseEntity.status(HttpStatus.OK).body(output);
	}
}
