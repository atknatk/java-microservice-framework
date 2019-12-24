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
import com.esys.bpm.dto.SqlTaskDto;
import com.esys.bpm.dto.TaskDto;
import com.esys.bpm.service.ISqlService;
import com.esys.bpm.utils.CommonUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("api/v1/sqlTask")
@Api(tags = { "sqlTask" })
public class SqlTaskController {
	@Autowired
	private ISqlService sqlService;

	@ApiOperation(notes = "If process is not saved, task will be just validated but not saved. It is because a process must exist in db in order to make changes. If task exists in db, it will first validate then updates task.", value = "Saves selected Sql Task information according to drag drop UI", response = BpmBaseResult.class, responseContainer = "SqlTaskDto")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns BpmBaseResult containing info, warning, error messages according to operation. Returns saved or validated SqlTaskDto object.", response = BpmBaseResult.class) })
	@PostMapping(path = "/saveTask")
	public ResponseEntity<BpmBaseResult> saveTask(
			@ApiParam(value = "Sql Task Dto object containing entire info", required = true) @RequestBody SqlTaskDto input) {

		/*
		 * input = new SqlTaskDto(); input.setXmlId("1_1_1"); input.setName("sql task");
		 * input.setDescription("test description"); input.setSqlType(SqlType.FUNCTION);
		 * input.setFunctionName("bpm.sendTest({Process.Id});");
		 * input.setNativeSql(null);
		 */

		BpmBaseResult output;

		if (CommonUtil.isSavedTask(input.getXmlId())) {
			output = sqlService.saveTask(input);
		} else {
			output = sqlService.validateTask(input);
		}

		return ResponseEntity.status(HttpStatus.OK).body(output);
	}

	@ApiOperation(value = "Gets selected Sql Task detail information")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns BpmBaseResult containing info, warning, error messages according to operation. Returns TaskDto object if task with given id is found.", response = BpmBaseResult.class) })
	@GetMapping(path = "/findById/{id}", produces = "application/json; charset=UTF-8")
	public ResponseEntity<BpmBaseResult<TaskDto>> findById(
			@ApiParam(value = "Sql Task xmlId", required = true) @PathVariable String id) {
		BpmBaseResult<TaskDto> output = sqlService.findById(CommonUtil.parseIdFromXmlId(id));

		return ResponseEntity.status(HttpStatus.OK).body(output);
	}
}
