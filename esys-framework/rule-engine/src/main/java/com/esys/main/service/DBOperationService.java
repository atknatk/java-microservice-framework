package com.esys.main.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.esys.main.controller.input.routine.ExecuteRoutineInput;
import com.esys.main.controller.output.OutputDTO;
import com.esys.main.controller.output.routine.RoutineDataOutput;

public interface DBOperationService {

	List<Object[]> executeSql(String sqlValue);

	List<Object[]> executeSql(String sqlValue, LinkedList<Object> parameterList);

	OutputDTO<List<RoutineDataOutput>> getRoutineData(String specific_schema);

	OutputDTO<List<Map<String, Object>>> executeRoutine(ExecuteRoutineInput input);


	
}
