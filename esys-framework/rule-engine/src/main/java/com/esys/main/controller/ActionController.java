package com.esys.main.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esys.main.controller.input.action.CreateActionInput;
import com.esys.main.controller.input.action.GetActionListInput;
import com.esys.main.controller.input.action.RemoveActionInput;
import com.esys.main.controller.input.action.UpdateActionInput;
import com.esys.main.controller.output.OutputDTO;
import com.esys.main.service.ActionService;
import com.esys.main.validations.ActionInputValidator;


@RestController
@RequestMapping("/rest/actionController")
public class ActionController extends BaseController{

	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(new ActionInputValidator());
	}
	
	@Autowired
	private ActionService actionService;

	@PostMapping(path = "/getActionList")
	public ResponseEntity<OutputDTO> getRuleActionList(@Valid @RequestBody GetActionListInput input,BindingResult bindingErrors) {
		OutputDTO  output = new OutputDTO();
		if (bindingErrors.hasErrors()) {
			output.setMessages(getValidationMessages(bindingErrors));
			return responseEntity(output);

		}
		 output = actionService.getActionList(input.getPageName(),input.getRuleName());
		return responseEntity(output);
	}
	
	@PostMapping(path = "/createAction")
	public ResponseEntity<OutputDTO> createAction(@Valid @RequestBody CreateActionInput input,BindingResult bindingErrors) {
		OutputDTO  output = new OutputDTO();
		if (bindingErrors.hasErrors()) {
			output.setMessages(getValidationMessages(bindingErrors));
			return responseEntity(output);

		}
		output = actionService.createAction(input);
		return responseEntity(output);
	}
	
	@PostMapping(path = "/removeAction")
	public ResponseEntity<OutputDTO> removeAction(@Valid @RequestBody RemoveActionInput input,BindingResult bindingErrors) {
		OutputDTO  output = new OutputDTO();
		if (bindingErrors.hasErrors()) {
			output.setMessages(getValidationMessages(bindingErrors));
			return responseEntity(output);

		}
		output = actionService.remove(input.getActionId());
		return responseEntity(output);
	}
	
	@PostMapping(path = "/updateAction")
	public ResponseEntity<OutputDTO> updateAction(@Valid @RequestBody UpdateActionInput input,BindingResult bindingErrors) {
		OutputDTO  output = new OutputDTO();
		if (bindingErrors.hasErrors()) {
			output.setMessages(getValidationMessages(bindingErrors));
			return responseEntity(output);

		}
		output = actionService.updateAction(input);
		return responseEntity(output);
	}
}
