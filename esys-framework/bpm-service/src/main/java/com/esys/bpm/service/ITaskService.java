package com.esys.bpm.service;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.dto.TaskDto;

public interface ITaskService {

	BpmBaseResult<?> validateTask(TaskDto task);

	BpmBaseResult<TaskDto> saveTask(TaskDto task);

	BpmBaseResult<TaskDto> findById(Long taskId);
}
