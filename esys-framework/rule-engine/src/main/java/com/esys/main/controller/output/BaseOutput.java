package com.esys.main.controller.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseOutput {

	private String returnCode;
	private String returnMessage;
	private String severity;
}
