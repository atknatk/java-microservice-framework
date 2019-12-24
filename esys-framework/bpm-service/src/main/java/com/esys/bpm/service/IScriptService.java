package com.esys.bpm.service;

import java.util.List;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.dto.ScriptTaskDetailDto;

@SuppressWarnings("rawtypes")
public interface IScriptService extends ITaskService {

	BpmBaseResult validateScriptDetail(String xmlId, ScriptTaskDetailDto detail);

	BpmBaseResult validateScriptDetails(String xmlId, List<ScriptTaskDetailDto> details);

}
