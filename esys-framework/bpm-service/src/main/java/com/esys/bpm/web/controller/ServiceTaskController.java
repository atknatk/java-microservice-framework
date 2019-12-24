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
import com.esys.bpm.dto.ServiceTaskDto;
import com.esys.bpm.dto.TaskDto;
import com.esys.bpm.service.IServiceService;
import com.esys.bpm.utils.CommonUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("api/v1/serviceTask")
@Api(tags = { "serviceTask" })
public class ServiceTaskController {

	@Autowired
	private IServiceService serviceService;

	@ApiOperation(notes = "If process is not saved, task will be just validated but not saved. It is because a process must exist in db in order to make changes. If task exists in db, it will first validate then updates task.", value = "Saves selected Service Task information according to drag drop UI", response = BpmBaseResult.class, responseContainer = "ServiceTaskDto")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns BpmBaseResult containing info, warning, error messages according to operation. Returns saved or validated ServiceTaskDto object.", response = BpmBaseResult.class) })
	@PostMapping(path = "/saveTask")
	public ResponseEntity<BpmBaseResult> saveTask(
			@ApiParam(value = "Service Task Dto object containing entire info", required = true) @RequestBody ServiceTaskDto input) {

		/*
		 * input = new ServiceTaskDto(); input.setXmlId("1_1_1");
		 * input.setName("task name"); input.setDescription("test description");
		 * input.setServiceUrl("http://service.cndyazilim.com");
		 * input.setMethodName("methodName"); input.setHeaders(new HashMap<String,
		 * String>() { { put("header1", "value1"); put("header2", "value2");
		 * put("header3", "value3"); } }); input.setParameters(new HashMap<String,
		 * String>() { { put("param1", "value1"); put("param2", "value2"); put("param3",
		 * "value3"); } });
		 */

		BpmBaseResult output;

		if (CommonUtil.isSavedTask(input.getXmlId())) {
			output = serviceService.saveTask(input);
		} else {
			output = serviceService.validateTask(input);
		}

		return ResponseEntity.status(HttpStatus.OK).body(output);
	}

	@ApiOperation(value = "Gets selected Service Task detail information")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns BpmBaseResult containing info, warning, error messages according to operation. Returns TaskDto object if task with given id is found.", response = BpmBaseResult.class) })
	@GetMapping(path = "/findById/{id}", produces = "application/json; charset=UTF-8")
	public ResponseEntity<BpmBaseResult<TaskDto>> findById(
			@ApiParam(value = "Service Task xmlId", required = true) @PathVariable String id) {

		BpmBaseResult<TaskDto> output = serviceService.findById(CommonUtil.parseIdFromXmlId(id));
		return ResponseEntity.status(HttpStatus.OK).body(output);
	}

}
