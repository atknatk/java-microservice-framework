package com.esys.bpm.service.impl;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.common.BpmLookup;
import com.esys.bpm.common.MessageText;
import com.esys.bpm.enums.ExpressionType;
import com.esys.bpm.enums.NotificationType;
import com.esys.bpm.enums.RoleType;
import com.esys.bpm.service.ICommonService;

@Service("commonService")
public class CommonServiceImpl implements ICommonService {

	@Override
	public BpmBaseResult<List<BpmLookup>> getNotificationSource(String notificationType) {
		BpmBaseResult<List<BpmLookup>> result = new BpmBaseResult<List<BpmLookup>>();

		List<BpmLookup> output = new ArrayList<BpmLookup>();
		if (notificationType.equals(NotificationType.EMAIL.toString()))
			output.add(new BpmLookup("1", "info@esys.com"));
		else if (notificationType.equals(NotificationType.SMS.toString()))
			output.add(new BpmLookup("1", "ESYS Corporate Phone"));
		else
			result.addErrorMessage(MessageText.INVALID_NOTIFICATIN_TYPE);

		result.setData(output);
		result.addWarningMessage("Dummy data is used because lack of notification service integration!");

		return result;
	}

	@Override
	public BpmBaseResult<List<ExpressionType>> getExpressionTypes() {
		BpmBaseResult<List<ExpressionType>> result = new BpmBaseResult<List<ExpressionType>>();
		result.setData(new ArrayList<ExpressionType>(EnumSet.allOf(ExpressionType.class)));
		return result;
	}

	@Override
	public BpmBaseResult<List<RoleType>> getRoleTypes() {
		BpmBaseResult<List<RoleType>> result = new BpmBaseResult<List<RoleType>>();
		result.setData(new ArrayList<RoleType>(EnumSet.allOf(RoleType.class)));

		return result;
	}

}
