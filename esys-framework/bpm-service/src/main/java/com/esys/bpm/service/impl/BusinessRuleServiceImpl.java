package com.esys.bpm.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.common.MessageText;
import com.esys.bpm.dto.BusinessRuleTaskDto;
import com.esys.bpm.dto.TaskDto;
import com.esys.bpm.entity.BusinessRuleTask;
import com.esys.bpm.repository.BusinessRuleTaskRepository;
import com.esys.bpm.service.IBusinessRuleService;
import com.esys.bpm.utils.StringUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service("businessRuleService")
public class BusinessRuleServiceImpl implements IBusinessRuleService {

	@Autowired
	private BusinessRuleTaskRepository businessRuleTaskRepository;

	@Override
	public BpmBaseResult validateTask(TaskDto task) {
		BusinessRuleTaskDto dto = (BusinessRuleTaskDto) task;

		BpmBaseResult result = new BpmBaseResult();

		if (StringUtil.isNullOrEmptyOrWhitespace(dto.getName()))
			result.addErrorMessage(dto.getXmlId(), MessageText.BUSINESS_RULE_TASK_EMPTY_NAME);
		if (StringUtil.isNullOrEmptyOrWhitespace(dto.getDescription()))
			result.addWarningMessage(dto.getXmlId(), MessageText.DESCRIPTION_EMPTY_WARNING);
		// TODO Rule engine'den valid rule id mi kontrolü yapılacak

		return result;
	}

	@Override
	public BpmBaseResult<TaskDto> saveTask(TaskDto task) {
		BusinessRuleTaskDto dto = (BusinessRuleTaskDto) task;

		BpmBaseResult<TaskDto> result = validateTask(dto);
		if (result.hasErrors())
			return result;
		else {
			try {
				BusinessRuleTask newEntity = dto.toEntity();
				BusinessRuleTask dbEntity = businessRuleTaskRepository.getOne(newEntity.getBusinessRuleTaskId());

				dbEntity.setName(newEntity.getName());
				dbEntity.setDescription(newEntity.getDescription());
				dbEntity.setRuleId(newEntity.getRuleId());

				// TODO değişenleri loglama

				dbEntity = businessRuleTaskRepository.save(dbEntity);
				result.setData(BusinessRuleTaskDto.toDto(dbEntity));
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
				Optional<BusinessRuleTask> entity = businessRuleTaskRepository.findById(taskId);
				if (!entity.isPresent())
					result.addErrorMessage(MessageText.NO_DATA_FOUND);
				else {
					result.setData(BusinessRuleTaskDto.toDto(entity.get()));
				}
			} catch (Exception ex) {
				result.addErrorMessage(ex.getMessage());
			}
		}
		return result;
	}

}
