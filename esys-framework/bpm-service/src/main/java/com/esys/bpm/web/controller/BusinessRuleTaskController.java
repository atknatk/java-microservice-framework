package com.esys.bpm.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.dto.BusinessRuleTaskDto;
import com.esys.bpm.dto.TaskDto;
import com.esys.bpm.service.IBusinessRuleService;
import com.esys.bpm.utils.CommonUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("api/v1/businessRuleTask")
@Api(tags = { "businessRuleTask" })
public class BusinessRuleTaskController {

	@Autowired
	private IBusinessRuleService businessRuleService;

	@ApiOperation(notes = "If process is not saved, task will be just validated but not saved. It is because a process must exist in db in order to make changes. If task exists in db, it will first validate then updates task.", value = "Saves selected Business Rule Task information according to drag drop UI", response = BpmBaseResult.class, responseContainer = "BusinessRuleTaskDto")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns BpmBaseResult containing info, warning, error messages according to operation. Returns saved or validated TaskDto object.", response = BpmBaseResult.class) })
	@PostMapping(path = "/saveTask", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BpmBaseResult> saveTask(
			@ApiParam(value = "Business Rule Task Dto object containing entire info", required = true) @RequestBody BusinessRuleTaskDto input) {

		/*
		 * input = new BusinessRuleTaskDto(); input.setXmlId("1_1_1");
		 * input.setName("Rule test"); input.setDescription("Description test");
		 * input.setRuleId(102L);
		 */

		BpmBaseResult output;

		if (CommonUtil.isSavedTask(input.getXmlId())) {
			output = businessRuleService.saveTask(input);
		} else {
			output = businessRuleService.validateTask(input);
		}

		return ResponseEntity.status(HttpStatus.OK).body(output);
	}

	@ApiOperation(value = "Gets selected Business Rule Task detail information")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns BpmBaseResult containing info, warning, error messages according to operation. Returns TaskDto object if task with given id is found.", response = BpmBaseResult.class) })
	@GetMapping(path = "/findById/{id}", produces = "application/json; charset=UTF-8")
	public ResponseEntity<BpmBaseResult<TaskDto>> findById(
			@ApiParam(value = "Business Rule Task xmlId", required = true) @PathVariable String id) {
		BpmBaseResult<TaskDto> output = businessRuleService.findById(CommonUtil.parseIdFromXmlId(id));

		return ResponseEntity.status(HttpStatus.OK).body(output);
	}
}
