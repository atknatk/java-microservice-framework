package com.esys.main.service;

import com.esys.main.controller.input.CallServiceInput;
import com.esys.main.controller.output.OutputDTO;
import com.esys.main.controller.output.action.CallServiceOutput;

public interface CallService {

	OutputDTO<CallServiceOutput> callRest(CallServiceInput input);
	
}
