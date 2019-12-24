package com.esys.bpm.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.common.MessageText;
import com.esys.bpm.dto.NotificationDetailDto;
import com.esys.bpm.dto.NotificationTaskDto;
import com.esys.bpm.dto.TaskDto;
import com.esys.bpm.entity.NotificationTask;
import com.esys.bpm.enums.NotificationType;
import com.esys.bpm.repository.NotificationDetailRepository;
import com.esys.bpm.repository.NotificationTaskRepository;
import com.esys.bpm.repository.ParticipantRepository;
import com.esys.bpm.service.INotificationService;
import com.esys.bpm.service.IParticipantService;
import com.esys.bpm.utils.CollectionUtil;
import com.esys.bpm.utils.StringUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service("notificationService")
public class NotificationServiceImpl implements INotificationService {

	@Autowired
	private NotificationTaskRepository notificationTaskRepository;

	@Autowired
	private NotificationDetailRepository notificationDetailRepository;

	@Autowired
	private ParticipantRepository participantRepository;

	@Autowired
	private IParticipantService participantService;

	@Override
	public BpmBaseResult validateNotificationDetail(String xmlId, NotificationDetailDto detail) {
		BpmBaseResult<Object> result = new BpmBaseResult<>();

		if (detail.getNotificationType() == null) {
			result.addErrorMessage(xmlId, MessageText.EMPTY_NOTIFICATION_TYPE);
		} else if (detail.getNotificationType().equals(NotificationType.EMAIL)) {
			if (StringUtil.isNullOrEmptyOrWhitespace(detail.getFrom()))
				result.addErrorMessage(xmlId, MessageText.EMPTY_FROM_SOURCE);
			if (CollectionUtil.isNullOrEmpty(detail.getToTargets()))
				result.addErrorMessage(xmlId, MessageText.EMPTY_TO_TARGET);
			else {
				BpmBaseResult participantResult = participantService.validateParticipants(xmlId, detail.getToTargets());
				participantResult.addMessages(participantResult.getMessages());
			}

			if (!CollectionUtil.isNullOrEmpty(detail.getCcTargets())) {
				BpmBaseResult participantResult = participantService.validateParticipants(xmlId, detail.getCcTargets());
				participantResult.addMessages(participantResult.getMessages());
			}
			if (!CollectionUtil.isNullOrEmpty(detail.getBccTargets())) {
				BpmBaseResult participantResult = participantService.validateParticipants(xmlId,
						detail.getBccTargets());
				participantResult.addMessages(participantResult.getMessages());
			}

			if (StringUtil.isNullOrEmptyOrWhitespace(detail.getSubject()))
				result.addWarningMessage(xmlId, MessageText.EMPTY);
			if (StringUtil.isNullOrEmptyOrWhitespace(detail.getBody()))
				result.addErrorMessage(xmlId, MessageText.EMPTY_BODY);

		} else if (detail.getNotificationType().equals(NotificationType.SMS))

		{
			if (CollectionUtil.isNullOrEmpty(detail.getToTargets()))
				result.addErrorMessage(xmlId, MessageText.EMPTY_TO_TARGET);
			else {
				BpmBaseResult participantResult = participantService.validateParticipants(xmlId, detail.getToTargets());
				participantResult.addMessages(participantResult.getMessages());
			}

			if (StringUtil.isNullOrEmptyOrWhitespace(detail.getBody()))
				result.addErrorMessage(xmlId, MessageText.EMPTY_BODY);
		}

		return result;
	}

	@Override
	public BpmBaseResult validateNotificationDetails(String xmlId, List<NotificationDetailDto> details) {
		BpmBaseResult result = new BpmBaseResult();

		if (CollectionUtil.isNullOrEmpty(details))
			result.addErrorMessage(MessageText.INVALID_NOTIFICATION_DETAIL);
		else {
			for (NotificationDetailDto detail : details) {
				BpmBaseResult detailResult = validateNotificationDetail(xmlId, detail);
				result.addMessages(detailResult.getMessages());
			}
		}

		return result;
	}

	@Override
	public BpmBaseResult validateTask(TaskDto task) {
		NotificationTaskDto dto = (NotificationTaskDto) task;

		BpmBaseResult result = new BpmBaseResult();

		if (StringUtil.isNullOrEmptyOrWhitespace(dto.getName()))
			result.addErrorMessage(dto.getXmlId(), MessageText.EMPTY);
		if (StringUtil.isNullOrEmptyOrWhitespace(dto.getDescription()))
			result.addWarningMessage(dto.getXmlId(), MessageText.EMPTY);
		if (CollectionUtil.isNullOrEmpty(dto.getNotificationDetails()))
			result.addWarningMessage(dto.getXmlId(), MessageText.EMPTY);

		BpmBaseResult detailResult = validateNotificationDetails(dto.getXmlId(), dto.getNotificationDetails());
		result.addMessages(detailResult.getMessages());

		return result;
	}

	@Override
	public BpmBaseResult<TaskDto> saveTask(TaskDto task) {
		NotificationTaskDto dto = (NotificationTaskDto) task;

		BpmBaseResult<TaskDto> result = validateTask(dto);
		if (result.hasErrors())
			return result;
		else {
			try {
				NotificationTask newEntity = dto.toEntity();
				NotificationTask dbEntity = notificationTaskRepository.findById(newEntity.getNotificationTaskId())
						.get();

				/*
				 * for (NotificationDetail detail : newEntity.getNotificationDetails()) { if
				 * (!CollectionUtil.isNullOrEmpty(detail.getToTargets())) { List<Participant>
				 * toParticipants = participantRepository.saveAll(detail.getToTargets());
				 * detail.setToTargets(toParticipants); } if
				 * (!CollectionUtil.isNullOrEmpty(detail.getCcTargets())) { List<Participant>
				 * ccParticipants = participantRepository.saveAll(detail.getCcTargets());
				 * detail.setToTargets(ccParticipants); } if
				 * (!CollectionUtil.isNullOrEmpty(detail.getBccTargets())) { List<Participant>
				 * bccParticipants = participantRepository.saveAll(detail.getBccTargets());
				 * detail.setToTargets(bccParticipants); } }
				 */

				notificationDetailRepository.saveAll(newEntity.getNotificationDetails());

				dbEntity.setName(newEntity.getName());
				dbEntity.setDescription(newEntity.getDescription());

				// TODO değişenleri loglama

				dbEntity = notificationTaskRepository.save(dbEntity);
				result.setData(NotificationTaskDto.toDto(dbEntity));
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
				Optional<NotificationTask> entity = notificationTaskRepository.findById(taskId);
				if (!entity.isPresent())
					result.addErrorMessage(MessageText.NO_DATA_FOUND);
				else {
					result.setData(NotificationTaskDto.toDto(entity.get()));
				}
			} catch (Exception ex) {
				result.addErrorMessage(ex.getMessage());
			}
		}

		return result;
	}

}
