package com.esys.bpm.service;

import java.util.List;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.dto.NotificationDetailDto;

@SuppressWarnings("rawtypes")
public interface INotificationService extends ITaskService {

	BpmBaseResult validateNotificationDetail(String xmlId, NotificationDetailDto detail);

	BpmBaseResult validateNotificationDetails(String xmlId, List<NotificationDetailDto> details);
}
