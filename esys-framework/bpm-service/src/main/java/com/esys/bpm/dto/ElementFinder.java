package com.esys.bpm.dto;

import com.esys.bpm.enums.ProcessComponent;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElementFinder {

	private Long id;
	private ProcessComponent component;

}
