package com.esys.main.dao;

import java.util.List;

import com.esys.main.entity.RengineAction;

public interface ActionDao {

	RengineAction saveOrUpdate(RengineAction rengineRuleAction);

	void remove(RengineAction action);

	List<RengineAction> getActions(Long pageId, Long pageRuleId);
	
	List<RengineAction> getActionsByPageId(Long pageId);
	
	List<RengineAction> getActionsByPageRuleId(Long pageRuleId);

	RengineAction getActionByActionName(String actionName);

	List<RengineAction> getActionsWithNullParameter(Long pageId, Long pageRuleId);

	RengineAction getActionByActionId(Long actionId);
}
