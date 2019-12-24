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
import com.esys.bpm.dto.ScriptTaskDto;
import com.esys.bpm.dto.TaskDto;
import com.esys.bpm.service.IScriptService;
import com.esys.bpm.utils.CommonUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("api/v1/scriptTask")
@Api(tags = { "scriptTask" })
public class ScriptTaskController {

	@Autowired
	private IScriptService scriptService;

	@ApiOperation(notes = "If process is not saved, task will be just validated but not saved. It is because a process must exist in db in order to make changes. If task exists in db, it will first validate then updates task.", value = "Saves selected Script Task information according to drag drop UI", response = BpmBaseResult.class, responseContainer = "ScriptTaskDto")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns BpmBaseResult containing info, warning, error messages according to operation. Returns saved or validated ScriptTaskDto object.", response = BpmBaseResult.class) })
	@PostMapping(path = "/saveTask")
	public ResponseEntity<BpmBaseResult> saveTask(
			@ApiParam(value = "Script Task Dto object containing entire info", required = true) @RequestBody ScriptTaskDto input) {

		/*
		 * input = new ScriptTaskDto(); input.setDescription("description");
		 * input.setXmlId("1_1_1"); input.setName("Script test");
		 * 
		 * ScriptTaskDetailDto detail1 = new ScriptTaskDetailDto();
		 * detail1.setNewValue("new value 1");
		 * detail1.setVariableName("variable Name 1");
		 * input.getScriptTaskDetails().add(detail1);
		 * 
		 * ScriptTaskDetailDto detail2 = new ScriptTaskDetailDto();
		 * detail2.setNewValue("new value 2");
		 * detail2.setVariableName("variable Name 2");
		 * input.getScriptTaskDetails().add(detail2);
		 */

		BpmBaseResult output;

		if (CommonUtil.isSavedTask(input.getXmlId())) {
			output = scriptService.saveTask(input);
		} else {
			output = scriptService.validateTask(input);
		}

		return ResponseEntity.status(HttpStatus.OK).body(output);
	}

	@ApiOperation(value = "Gets selected Script Task detail information")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns BpmBaseResult containing info, warning, error messages according to operation. Returns TaskDto object if task with given id is found.", response = BpmBaseResult.class) })
	@GetMapping(path = "/findById/{id}", produces = "application/json; charset=UTF-8")
	public ResponseEntity<BpmBaseResult<TaskDto>> findById(
			@ApiParam(value = "Script Task xmlId", required = true) @PathVariable String id) {
		BpmBaseResult<TaskDto> output = scriptService.findById(CommonUtil.parseIdFromXmlId(id));

		return ResponseEntity.status(HttpStatus.OK).body(output);
	}
}
