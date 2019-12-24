package com.esys.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esys.main.controller.input.CallServiceInput;
import com.esys.main.controller.output.OutputDTO;
import com.esys.main.service.CallService;

@RestController
@RequestMapping("/rest/actionController")
public class CallServiceController extends BaseController{

	@Autowired
	private CallService callService;

	@PostMapping(path = "/callRequest")
	public ResponseEntity<OutputDTO> callRequest(@RequestBody CallServiceInput input) {
		OutputDTO  output = callService.callRest(input);
		return responseEntity(output);
	}
}
