package com.esys.bpm.service;

import java.util.List;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.common.BpmLookup;
import com.esys.bpm.enums.ExpressionType;
import com.esys.bpm.enums.RoleType;

public interface ICommonService {

	BpmBaseResult<List<BpmLookup>> getNotificationSource(String notificationType);

	BpmBaseResult<List<ExpressionType>> getExpressionTypes();

	BpmBaseResult<List<RoleType>> getRoleTypes();
}
