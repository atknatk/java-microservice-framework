package com.esys.main.service;

import com.esys.main.controller.input.action.UpdateActionInput;
import com.esys.main.controller.output.OutputDTO;
import com.esys.main.dto.ActionDTO;

public interface ActionService {

	OutputDTO getActionList(String pageName, String ruleName);

	OutputDTO createAction(ActionDTO input);

	OutputDTO<ActionDTO> updateAction(UpdateActionInput input);

	OutputDTO remove(Long actionId);
}
