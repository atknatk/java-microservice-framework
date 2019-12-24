package com.esys.bpm.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.common.MessageText;
import com.esys.bpm.dto.TaskDto;
import com.esys.bpm.dto.UserTaskDto;
import com.esys.bpm.entity.UserTask;
import com.esys.bpm.repository.NotificationDetailRepository;
import com.esys.bpm.repository.ParticipantRepository;
import com.esys.bpm.repository.UserTaskRepository;
import com.esys.bpm.service.INotificationService;
import com.esys.bpm.service.IParticipantService;
import com.esys.bpm.service.IUserService;
import com.esys.bpm.utils.StringUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service("userService")
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserTaskRepository userTaskRepository;

	@Autowired
	private ParticipantRepository participantRepository;

	@Autowired
	private NotificationDetailRepository notificationDetailRepository;

	@Autowired
	private IParticipantService participantService;

	@Autowired
	private INotificationService notificationService;

	@Override
	public BpmBaseResult validateTask(TaskDto task) {
		UserTaskDto dto = (UserTaskDto) task;

		BpmBaseResult<Object> result = new BpmBaseResult<>();

		if (StringUtil.isNullOrEmptyOrWhitespace(dto.getName()))
			result.addErrorMessage(dto.getXmlId(), MessageText.USER_TASK_EMPTY_NAME);
		if (StringUtil.isNullOrEmptyOrWhitespace(dto.getDescription()))
			result.addWarningMessage(dto.getXmlId(), MessageText.DESCRIPTION_EMPTY_WARNING);
		if (CollectionUtils.isEmpty(dto.getAssignees())) {
			result.addErrorMessage(dto.getXmlId(), MessageText.EMPTY_ASSIGNEE);
		} else {
			BpmBaseResult participantResult = participantService.validateParticipants(dto.getXmlId(),
					dto.getAssignees());
			result.addMessages(participantResult.getMessages());

		}

		if (CollectionUtils.isEmpty(dto.getNotificationDetails())) {
			result.addWarningMessage(MessageText.EMPTY_NOTIFICATION);
		} else {
			BpmBaseResult notificationResult = notificationService.validateNotificationDetails(dto.getXmlId(),
					dto.getNotificationDetails());
			result.addMessages(notificationResult.getMessages());
		}

		return result;
	}

	@Override
	public BpmBaseResult<TaskDto> saveTask(TaskDto task) {
		UserTaskDto dto = (UserTaskDto) task;

		BpmBaseResult<TaskDto> result = validateTask(dto);
		if (result.hasErrors())
			return result;
		else {
			try {
				UserTask newEntity = dto.toEntity();
				UserTask dbEntity = userTaskRepository.findById(newEntity.getUserTaskId()).get();

				dbEntity.setName(newEntity.getName());
				dbEntity.setDescription(newEntity.getDescription());

				participantRepository.saveAll(newEntity.getAssignees());
				notificationDetailRepository.saveAll(newEntity.getNotificationDetails());

				// TODO değişenleri loglama

				dbEntity = userTaskRepository.save(dbEntity);
				result.setData(UserTaskDto.toDto(dbEntity));
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
				Optional<UserTask> entity = userTaskRepository.findById(taskId);
				if (!entity.isPresent())
					result.addErrorMessage(MessageText.NO_DATA_FOUND);
				else {
					result.setData(UserTaskDto.toDto(entity.get()));
				}
			} catch (Exception ex) {
				result.addErrorMessage(ex.getMessage());
			}
		}

		return result;
	}

}
