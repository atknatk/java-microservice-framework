package com.esys.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esys.main.controller.input.ExecuteRuleEngineInput;
import com.esys.main.controller.output.OutputDTO;
import com.esys.main.service.ExecuteEngineService;

@RestController
@RequestMapping("/rest/executeEngineController")
public class ExecuteEngineController extends BaseController{

	@Autowired
	private ExecuteEngineService executeEngineService;

	@PostMapping(path = "/executeRuleEngine")
	public ResponseEntity<OutputDTO> executeRuleEngine(@RequestBody ExecuteRuleEngineInput input) {
		OutputDTO response = executeEngineService.executeRuleEngine(input);
		return responseEntity(response);
	}
}
