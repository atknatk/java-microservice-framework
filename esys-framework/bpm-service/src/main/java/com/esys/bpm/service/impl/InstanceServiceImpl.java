package com.esys.bpm.service.impl;

import org.springframework.stereotype.Service;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.entity.ProcessId;
import com.esys.bpm.service.IInstanceService;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service("instanceService")
public class InstanceServiceImpl implements IInstanceService {

	@Override
	public BpmBaseResult onInstanceCreated(ProcessId processId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BpmBaseResult onTaskAssigned() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BpmBaseResult onTaskCompleted() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BpmBaseResult onInstanceCompleted() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BpmBaseResult onInstanceUpdated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BpmBaseResult onTaskUpdated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BpmBaseResult onSubTaskUpdated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BpmBaseResult onStageComplete() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getNextTaskId(String outcome, boolean isStartTask) {
		// TODO Auto-generated method stub
		return null;
	}

}
