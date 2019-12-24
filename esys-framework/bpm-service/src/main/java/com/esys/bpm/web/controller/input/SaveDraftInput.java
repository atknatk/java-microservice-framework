package com.esys.bpm.web.controller.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveDraftInput {

	private String id;
	private String name;
	private String xml;
}