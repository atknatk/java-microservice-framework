package com.esys.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esys.main.controller.output.OutputDTO;
import com.esys.main.dto.PageDTO;
import com.esys.main.service.PageService;

@RestController
@RequestMapping("/rest/pageController")
public class PageController extends BaseController{

	@Autowired
	private PageService pageService;

	@PostMapping(path = "/createPage")
	public ResponseEntity<OutputDTO> createPage(@RequestBody PageDTO input) {
		OutputDTO output = pageService.createPage(input);
		return responseEntity(output);
	}
	
	@PostMapping(path = "/removePage")
	public ResponseEntity<OutputDTO> removePage(@RequestBody PageDTO input) {
		OutputDTO output = pageService.removePage(input);
		return responseEntity(output);
	}
	
	@PostMapping(path = "/updatePage")
	public ResponseEntity<OutputDTO> updatePage(@RequestBody PageDTO input) {
		OutputDTO output = pageService.updatePage(input);
		return responseEntity(output);
	}
	
	@PostMapping(path = "/getPages")
	public ResponseEntity<OutputDTO> getPages(Pageable pagable) {
		OutputDTO output = pageService.getPages(pagable);
		return responseEntity(output);
	}

}
