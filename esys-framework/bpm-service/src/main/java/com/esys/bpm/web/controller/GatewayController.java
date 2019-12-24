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
import com.esys.bpm.dto.GatewayDto;
import com.esys.bpm.service.IGatewayService;
import com.esys.bpm.utils.CommonUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("api/v1/gatewayTask")
@Api(tags = { "gatewayTask" })
public class GatewayController {

	@Autowired
	private IGatewayService gatewayService;

	@ApiOperation(notes = "If process is not saved, task will be just validated but not saved. It is because a process must exist in db in order to make changes. If task exists in db, it will first validate then updates task.", value = "Saves selected Gateway information according to drag drop UI", response = BpmBaseResult.class, responseContainer = "GatewayDto")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns BpmBaseResult containing info, warning, error messages according to operation. Returns saved or validated GatewayDto object.", response = BpmBaseResult.class) })
	@PostMapping(path = "/saveGateway")
	public ResponseEntity<BpmBaseResult> saveGateway(
			@ApiParam(value = "Gateway Dto object containing entire info", required = true) @RequestBody GatewayDto input) {

		BpmBaseResult output;

		if (CommonUtil.isSavedTask(input.getXmlId())) {
			output = gatewayService.saveGateway(input);
		} else {
			output = gatewayService.validateGateway(input);
		}

		return ResponseEntity.status(HttpStatus.OK).body(output);
	}

	@ApiOperation(value = "Gets selected Gateway Task detail information")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns BpmBaseResult containing info, warning, error messages according to operation. Returns TaskDto object if task with given id is found.", response = BpmBaseResult.class) })
	@GetMapping(path = "/findById/{id}", produces = "application/json; charset=UTF-8")
	public ResponseEntity<BpmBaseResult<GatewayDto>> findById(
			@ApiParam(value = "Gateway xmlId", required = true) @PathVariable String id) {
		BpmBaseResult<GatewayDto> output = gatewayService.findById(CommonUtil.parseIdFromXmlId(id));

		return ResponseEntity.status(HttpStatus.OK).body(output);
	}

}
