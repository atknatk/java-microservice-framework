package com.esys.bpm.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.dto.DraftDto;
import com.esys.bpm.service.IDraftService;
import com.esys.bpm.web.controller.input.SaveDraftComponentInput;
import com.esys.bpm.web.controller.input.SaveDraftInput;

@RestController
@RequestMapping("api/v1/draft")
public class DraftController {

	@Autowired
	private IDraftService draftService;

	@PostMapping(path = "/saveDraft")
	public ResponseEntity<BpmBaseResult<Long>> saveDraft(@RequestBody SaveDraftInput input) {
		/*
		 * SaveDraftInput sdi = new SaveDraftInput(); sdi.setId("-1");
		 * sdi.setName("furkan test"); sdi.setXml("test xml");
		 */
		BpmBaseResult<Long> output = draftService.saveDraft(new Long(input.getId()), input.getName(), input.getXml());

		return ResponseEntity.status(HttpStatus.OK).body(output);
	}

	@PostMapping(path = "/saveDraftComponent")
	public ResponseEntity<BpmBaseResult<Long>> saveDraftComponent(@RequestBody SaveDraftComponentInput input) {

		/*
		 * SaveDraftComponentInput sdci = new SaveDraftComponentInput();
		 * sdci.setDraftId("1"); sdci.setXml("xml"); sdci.setXmlId("xmlId");
		 */

		BpmBaseResult<Long> output = draftService.saveDraftComponent(new Long(input.getDraftId()), input.getXmlId(),
				input.getXml());

		return ResponseEntity.status(HttpStatus.OK).body(output);
	}

	@GetMapping(path = "/getDrafts", produces = "application/json; charset=UTF-8")
	public ResponseEntity<BpmBaseResult<List<DraftDto>>> getDrafts() {
		/*
		 * BpmBaseResult<List<DraftDto>> output = new BpmBaseResult<List<DraftDto>>();
		 * List<DraftDto> list = new ArrayList<DraftDto>(); DraftDto dto = new
		 * DraftDto(); dto.setCreateDate(LocalDateTime.now());
		 * dto.setCreateUser("ESYS"); dto.setDraftId(1L);
		 * dto.setLastModifiedDate(LocalDateTime.now());
		 * dto.setLastModifiedUser("ESYS"); dto.setName("Process 1");
		 * dto.setResponsibleUser("ESYS"); dto.setStatus("DRAFT"); dto.setVersion(1L);
		 * dto.setXml("process xml"); List<String> components = new ArrayList<String>();
		 * components.add("component 1 xml"); components.add("component 2 xml");
		 * dto.setComponents(components);
		 * 
		 * list.add(dto); output.setData(list);
		 */

		BpmBaseResult<List<DraftDto>> output = draftService.getDrafts();
		return ResponseEntity.status(HttpStatus.OK).body(output);
	}
}
