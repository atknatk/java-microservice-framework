package com.esys.bpm.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.common.BpmLookup;
import com.esys.bpm.common.MessageText;
import com.esys.bpm.dto.ServiceTaskDto;
import com.esys.bpm.dto.TaskDto;
import com.esys.bpm.entity.ServiceTask;
import com.esys.bpm.repository.ServiceTaskRepository;
import com.esys.bpm.service.IServiceService;
import com.esys.bpm.utils.StringUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service("serviceService")
public class ServiceServiceImpl implements IServiceService {

	@Autowired
	private ServiceTaskRepository serviceTaskRepository;

	@Override
	public BpmBaseResult validateTask(TaskDto task) {
		ServiceTaskDto dto = (ServiceTaskDto) task;

		BpmBaseResult result = new BpmBaseResult();

		if (StringUtil.isNullOrEmptyOrWhitespace(dto.getName()))
			result.addErrorMessage(dto.getXmlId(), MessageText.SERVICE_TASK_EMPTY_NAME);
		if (StringUtil.isNullOrEmptyOrWhitespace(dto.getDescription()))
			result.addWarningMessage(dto.getXmlId(), MessageText.DESCRIPTION_EMPTY_WARNING);
		if (StringUtil.isValidUrl(dto.getServiceUrl()))
			result.addErrorMessage(dto.getXmlId(), MessageText.INVALID_URL);
		if (StringUtil.isNullOrEmptyOrWhitespace(dto.getMethodName()))
			result.addErrorMessage(dto.getXmlId(), MessageText.INVALID_METHOD_NAME);
		if (dto.getHeaders() != null) {
			for (BpmLookup lookup : dto.getHeaders()) {
				if (StringUtil.isNullOrEmptyOrWhitespace(lookup.getCode().toString()))
					result.addErrorMessage(dto.getXmlId(), MessageText.INVALID_HTTP_HEADER_KEY);
				if (StringUtil.isNullOrEmptyOrWhitespace(lookup.getValue().toString()))
					result.addErrorMessage(dto.getXmlId(), MessageText.INVALID_HTTP_HEADER_VALUE);
			}
		}
		if (dto.getParameters() != null) {
			for (BpmLookup lookup : dto.getParameters()) {
				if (StringUtil.isNullOrEmptyOrWhitespace(lookup.getCode().toString()))
					result.addErrorMessage(dto.getXmlId(), MessageText.INVALID_HTTP_PARAMETER_KEY);
				if (StringUtil.isNullOrEmptyOrWhitespace(lookup.getValue().toString()))
					result.addErrorMessage(dto.getXmlId(), MessageText.INVALID_HTTP_PARAMETER_VALUE);
			}
		}

		return result;
	}

	@Override
	public BpmBaseResult<TaskDto> saveTask(TaskDto task) {
		ServiceTaskDto dto = (ServiceTaskDto) task;

		BpmBaseResult<TaskDto> result = validateTask(dto);
		if (result.hasErrors())
			return result;
		else {
			try {
				ServiceTask newEntity = dto.toEntity();
				ServiceTask dbEntity = serviceTaskRepository.findById(newEntity.getServiceTaskId()).get();

				dbEntity.setName(newEntity.getName());
				dbEntity.setDescription(newEntity.getDescription());
				dbEntity.setServiceUrl(newEntity.getServiceUrl());
				dbEntity.setMethodName(newEntity.getMethodName());
				dbEntity.setHeaders(newEntity.getHeaders());
				dbEntity.setParameters(newEntity.getParameters());

				// TODO değişenleri loglama

				dbEntity = serviceTaskRepository.save(dbEntity);
				result.setData(ServiceTaskDto.toDto(dbEntity));
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
				Optional<ServiceTask> entity = serviceTaskRepository.findById(taskId);
				if (!entity.isPresent())
					result.addErrorMessage(MessageText.NO_DATA_FOUND);
				else {
					result.setData(ServiceTaskDto.toDto(entity.get()));
				}
			} catch (Exception ex) {
				result.addErrorMessage(ex.getMessage());
			}
		}
		return result;
	}

}
