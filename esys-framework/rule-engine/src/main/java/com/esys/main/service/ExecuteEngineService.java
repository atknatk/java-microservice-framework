package com.esys.main.service;

import com.esys.main.controller.input.ExecuteRuleEngineInput;
import com.esys.main.controller.output.OutputDTO;
import com.esys.main.controller.output.action.BaseActionOutput;

public interface ExecuteEngineService {

	OutputDTO<BaseActionOutput> executeRuleEngine(ExecuteRuleEngineInput input);
	
}
