package com.esys.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esys.main.controller.input.PageInfoSaveInput;
import com.esys.main.controller.output.BaseOutput;
import com.esys.main.dto.PageInfoDTO;
import com.esys.main.entity.RenginePageInfo;
import com.esys.main.enums.SeverityEnum;
import com.esys.main.service.PageInfoService;

@RestController
@RequestMapping("/rest/pageInfoController")
public class PageInfoController extends BaseController{

	@Autowired
	private PageInfoService pageInfoService;

	@PostMapping(path = "/savePageInfo")
	public ResponseEntity<BaseOutput> savePageInfo(@RequestBody PageInfoSaveInput pageInfoSaveInput) {
		pageInfoService.save(pageInfoSaveInput);
		
		BaseOutput output = new BaseOutput();
		output.setReturnCode(SeverityEnum.CREATE.value());
		output.setReturnMessage("Page Info Create Sucsess...");
		return responseEntity(output);
	}
	
	@PostMapping(path = "/getPageInfo")
	public ResponseEntity<List<PageInfoDTO>> getPageInfo(@RequestBody PageInfoDTO pageInfoDTO) {
		List<RenginePageInfo> result = pageInfoService.getPageInfoByPageId(pageInfoDTO.getPageId());
//		
//		MessageOutput output = new MessageOutput();
//		output.setReturnCode(SeverityEnum.CREATE.value());
//		output.setReturnMessage("Page Create Sucsess...");
		return responseEntity(PageInfoDTO.toDTOList(result));
	}

}
