package com.esys.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esys.main.controller.output.BaseOutput;
import com.esys.main.controller.output.OutputDTO;
import com.esys.main.dto.PageRuleDTO;
import com.esys.main.service.PageRuleService;


@RestController
@RequestMapping("/rest/pageRuleController")
public class PageRuleController extends BaseController{

	@Autowired
	private PageRuleService pageRuleService;

	@PostMapping(path = "/createRule")
	public ResponseEntity<OutputDTO> createRule(@RequestBody PageRuleDTO pageRuleDTO) {
		OutputDTO output = pageRuleService.createRule(pageRuleDTO);
		return responseEntity(output);
	}
	
	@PostMapping(path = "/removeRule")
	public ResponseEntity<OutputDTO> removeRule(@RequestBody PageRuleDTO input) {
		OutputDTO output = pageRuleService.removeRule(input);
		return responseEntity(output);
	}
	
	@PostMapping(path = "/updateRule")
	public ResponseEntity<OutputDTO> updateRule(@RequestBody PageRuleDTO input) {
		OutputDTO output = pageRuleService.updateRule(input);
		return responseEntity(output);
	}
	
	@PostMapping(path = "/getPageRules")
	public ResponseEntity<List<PageRuleDTO>> getPageRules(@RequestBody PageRuleDTO input,Pageable pagable) {
		OutputDTO  output = pageRuleService.getPageRules(input.getPageId(),pagable);
		return responseEntity(output);
	}

	@PostMapping(path = "/updateRuleOrder")
	public ResponseEntity<BaseOutput> updateRuleOrder(@RequestBody PageRuleDTO input) {
		OutputDTO output = pageRuleService.updateRuleOrder(input);
		return responseEntity(output);
	}


}
