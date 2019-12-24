package com.esys.main.controller.output.routine;

import java.util.List;

import lombok.Data;

@Data
public class RoutineDataOutput {

	private String routineName;
	private String routineType;
	private String returnDataType;
	private List<RoutineParameterOutput> routineParameters;
}
