package com.esys.bpm.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.common.MessageText;
import com.esys.bpm.dto.TaskDto;
import com.esys.bpm.dto.TimerTaskDto;
import com.esys.bpm.entity.TimerTask;
import com.esys.bpm.enums.TimerType;
import com.esys.bpm.repository.TimerTaskRepository;
import com.esys.bpm.service.ITimerService;
import com.esys.bpm.utils.StringUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service("timerService")
public class TimerServiceImpl implements ITimerService {

	@Autowired
	private TimerTaskRepository timerTaskRepository;

	@Override
	public BpmBaseResult validateTask(TaskDto task) {
		TimerTaskDto dto = (TimerTaskDto) task;

		BpmBaseResult result = new BpmBaseResult();

		if (StringUtil.isNullOrEmptyOrWhitespace(dto.getName()))
			result.addErrorMessage(dto.getXmlId(), MessageText.TIMER_TASK_EMPTY_NAME);
		if (StringUtil.isNullOrEmptyOrWhitespace(dto.getDescription()))
			result.addWarningMessage(dto.getXmlId(), MessageText.DESCRIPTION_EMPTY_WARNING);

		if (dto.getTimerType() == null)
			result.addErrorMessage(dto.getXmlId(), MessageText.INVALID_TIMER_TYPE);
		else if (dto.getTimerType().equals(TimerType.INTERVAL) && dto.getInterval() < 1)
			result.addErrorMessage(dto.getXmlId(), MessageText.INVALID_TIMER_INTERVAL);
		else if (dto.getTimerType().equals(TimerType.SPECIFIC_DATE) && dto.getSpecificDate() == null)
			result.addErrorMessage(dto.getXmlId(), MessageText.INVALID_TIMER_SPECIFIC_DATE);
		else if (dto.getTimerType().equals(TimerType.SCHEDULE) && dto.getInterval() < 1)
			result.addErrorMessage(dto.getXmlId(), MessageText.INVALID_TIMER_SCHEDULE);

		return result;
	}

	@Override
	public BpmBaseResult<TaskDto> saveTask(TaskDto task) {
		TimerTaskDto dto = (TimerTaskDto) task;

		BpmBaseResult<TaskDto> result = validateTask(dto);
		if (result.hasErrors())
			return result;
		else {
			try {
				TimerTask newEntity = dto.toEntity();
				TimerTask dbEntity = timerTaskRepository.findById(newEntity.getTimerTaskId()).get();

				dbEntity.setName(newEntity.getName());
				dbEntity.setDescription(newEntity.getDescription());
				dbEntity.setTimerTaskId(newEntity.getTimerTaskId());
				dbEntity.setTimerType(newEntity.getTimerType());
				dbEntity.setSpecificDate(newEntity.getSpecificDate());
				dbEntity.setInterval(newEntity.getInterval());
				dbEntity.setSchedule(newEntity.getSchedule());

				// TODO değişenleri loglama

				dbEntity = timerTaskRepository.save(dbEntity);
				result.setData(TimerTaskDto.toDto(dbEntity));
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
				Optional<TimerTask> entity = timerTaskRepository.findById(taskId);
				if (!entity.isPresent())
					result.addErrorMessage(MessageText.NO_DATA_FOUND);
				else {
					result.setData(TimerTaskDto.toDto(entity.get()));
				}
			} catch (Exception ex) {
				result.addErrorMessage(ex.getMessage());
			}
		}

		return result;
	}

}
