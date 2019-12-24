package com.esys.main.controller.output.routine;

import lombok.Data;

@Data
public class RoutineParameterOutput {
	private String parameterName;
	private String parameterDataType;
	private int ordinal;
	private String parameterMode;
}
