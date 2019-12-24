package com.esys.bpm.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.common.MessageText;
import com.esys.bpm.dto.ScriptTaskDetailDto;
import com.esys.bpm.dto.ScriptTaskDto;
import com.esys.bpm.dto.TaskDto;
import com.esys.bpm.entity.ScriptTask;
import com.esys.bpm.repository.ScriptTaskDetailRepository;
import com.esys.bpm.repository.ScriptTaskRepository;
import com.esys.bpm.service.IScriptService;
import com.esys.bpm.utils.CollectionUtil;
import com.esys.bpm.utils.StringUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service("scriptService")
public class ScriptServiceImpl implements IScriptService {

	@Autowired
	private ScriptTaskRepository scriptTaskRepository;

	@Autowired
	private ScriptTaskDetailRepository scriptTaskDetailRepository;

	@Override
	public BpmBaseResult validateScriptDetail(String xmlId, ScriptTaskDetailDto detail) {
		BpmBaseResult<Object> result = new BpmBaseResult<>();

		if (StringUtil.isNullOrEmptyOrWhitespace(detail.getVariableName()))
			result.addErrorMessage(xmlId, MessageText.EMPTY_SCRIPT_TASK_VARIABLE_NAME);
		if (StringUtil.isNullOrEmptyOrWhitespace(detail.getNewValue()))
			result.addErrorMessage(xmlId, MessageText.EMPTY_SCRIPT_TASK_NEW_VALUE);

		return result;
	}

	@Override
	public BpmBaseResult validateScriptDetails(String xmlId, List<ScriptTaskDetailDto> details) {
		BpmBaseResult result = new BpmBaseResult();

		if (CollectionUtil.isNullOrEmpty(details))
			result.addErrorMessage(xmlId, MessageText.INVALID_SCRIPT_DETAIL);
		else {
			for (ScriptTaskDetailDto detail : details) {
				BpmBaseResult detailResult = validateScriptDetail(xmlId, detail);
				result.addMessages(detailResult.getMessages());
			}
		}

		return result;
	}

	@Override
	public BpmBaseResult validateTask(TaskDto task) {
		ScriptTaskDto dto = (ScriptTaskDto) task;

		BpmBaseResult result = new BpmBaseResult();

		if (StringUtil.isNullOrEmptyOrWhitespace(dto.getName()))
			result.addErrorMessage(dto.getXmlId(), MessageText.SCRIPT_TASK_EMPTY_NAME);
		if (StringUtil.isNullOrEmptyOrWhitespace(dto.getDescription()))
			result.addWarningMessage(dto.getXmlId(), MessageText.DESCRIPTION_EMPTY_WARNING);
		BpmBaseResult detailResult = validateScriptDetails(dto.getXmlId(), dto.getScriptTaskDetails());
		result.addMessages(detailResult.getMessages());

		return result;
	}

	@Override
	public BpmBaseResult<TaskDto> saveTask(TaskDto task) {
		ScriptTaskDto dto = (ScriptTaskDto) task;

		BpmBaseResult<TaskDto> result = validateTask(dto);
		if (result.hasErrors())
			return result;
		else {
			try {
				ScriptTask newEntity = dto.toEntity();
				ScriptTask dbEntity = scriptTaskRepository.findById(newEntity.getScriptTaskId()).get();

				dbEntity.setName(newEntity.getName());
				dbEntity.setDescription(newEntity.getDescription());

				scriptTaskDetailRepository.saveAll(newEntity.getScriptTaskDetails());

				// TODO değişenleri loglama

				dbEntity = scriptTaskRepository.save(dbEntity);
				result.setData(ScriptTaskDto.toDto(dbEntity));
			} catch (Exception ex) {
				result.addErrorMessage(ex.getMessage());
			}
		}
		return result;
	}

	@Override
	public BpmBaseResult<TaskDto> findById(Long taskId) {
		BpmBaseResult<TaskDto> result = new BpmBaseResult<TaskDto>();

		if (taskId == null)
			result.addErrorMessage(MessageText.NO_DATA_FOUND);
		else {
			try {
				Optional<ScriptTask> entity = scriptTaskRepository.findById(taskId);
				if (!entity.isPresent())
					result.addErrorMessage(MessageText.NO_DATA_FOUND);
				else {
					result.setData(ScriptTaskDto.toDto(entity.get()));
				}
			} catch (Exception ex) {
				result.addErrorMessage(ex.getMessage());
			}
		}

		return result;
	}

}
