package com.esys.bpm.service;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.entity.ProcessId;

public interface IInstanceService {

	BpmBaseResult onInstanceCreated(ProcessId processId);

	BpmBaseResult onTaskAssigned();

	BpmBaseResult onTaskCompleted();

	BpmBaseResult onInstanceCompleted();

	BpmBaseResult onInstanceUpdated();

	BpmBaseResult onTaskUpdated();

	BpmBaseResult onSubTaskUpdated();

	BpmBaseResult onStageComplete();

	Long getNextTaskId(String outcome, boolean isStartTask);

}
