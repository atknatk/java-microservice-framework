package com.esys.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esys.main.controller.input.routine.ExecuteRoutineInput;
import com.esys.main.controller.input.routine.RoutineInput;
import com.esys.main.controller.output.OutputDTO;
import com.esys.main.service.DBOperationService;

@RestController
@RequestMapping("/rest/DBOperationController")
public class DBOperationController extends BaseController{

	@Autowired
	private DBOperationService dBOperationService;
	
	@PostMapping(path = "/getRoutineData")
	public ResponseEntity<OutputDTO> getRoutineData(@RequestBody RoutineInput input) {
		OutputDTO  output = dBOperationService.getRoutineData(input.getSpecificSchema());
		return responseEntity(output);
	}
	
	@PostMapping(path = "/executeRoutine")
	public ResponseEntity<OutputDTO> executeRoutine(@RequestBody ExecuteRoutineInput input) {
		OutputDTO  output = dBOperationService.executeRoutine(input);
		return responseEntity(output);
	}
}
