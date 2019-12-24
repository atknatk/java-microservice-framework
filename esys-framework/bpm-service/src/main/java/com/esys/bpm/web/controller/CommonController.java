package com.esys.bpm.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.common.BpmLookup;
import com.esys.bpm.enums.ExpressionType;
import com.esys.bpm.enums.RoleType;
import com.esys.bpm.service.ICommonService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("api/v1/common")
@Api(tags = { "common" })
public class CommonController {

	@Autowired
	private ICommonService commonService;

	@ApiOperation(value = "Gets expression type enum values. Currently NAME, OBJECT, and SQL")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns expression type enum values as String list", response = ExpressionType.class) })
	@GetMapping(path = "/getExpressionTypes", produces = "application/json; charset=UTF-8")
	public BpmBaseResult<List<ExpressionType>> getExpressionTypes() {
		return commonService.getExpressionTypes();
	}

	@ApiOperation(value = "Gets expression type enum values. Currently USER, USERGROUP, BPMROLE and SYSTEMROLE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns expression type enum values as String list", response = RoleType.class) })
	@GetMapping(path = "/getRoleTypes", produces = "application/json; charset=UTF-8")
	public BpmBaseResult<List<RoleType>> getRoleTypes() {
		return commonService.getRoleTypes();
	}

	@ApiOperation(value = "Gets notification source according to notification type")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returns notification sources (from) as BpmLookup list", response = BpmLookup.class) })
	@GetMapping(path = "/getNotificationSources/{notificationType}", produces = "application/json; charset=UTF-8")
	public BpmBaseResult<List<BpmLookup>> getNotificationSources(
			@ApiParam(value = "Notification type name", required = true) @PathVariable String notificationType) {
		return commonService.getNotificationSource(notificationType);
	}
}
