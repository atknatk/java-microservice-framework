package com.esys.main.controller.input.routine;

import java.util.List;

import lombok.Data;

@Data
public class ExecuteRoutineInput {

	private String specificSchema;
	private String routineName;
	private List<RoutineParameterInput> parameters;
}
