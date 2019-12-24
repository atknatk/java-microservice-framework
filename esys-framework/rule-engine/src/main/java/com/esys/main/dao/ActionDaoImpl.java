package com.esys.main.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.esys.main.entity.RengineAction;
import com.esys.main.repository.ActionRepository;
import com.esys.main.utils.JwtTokenDataHolder;

@Repository
public class ActionDaoImpl implements ActionDao {
	
	
	@Autowired
	private ActionRepository ruleActionRepository;
	
	@Override
	public RengineAction saveOrUpdate(RengineAction rengineRuleAction) {
		return ruleActionRepository.save(rengineRuleAction);
	}
	
	@Override
	public void remove(RengineAction rengineRuleAction) {
		ruleActionRepository.delete(rengineRuleAction);
	}

	@Override
	public List<RengineAction> getActionsWithNullParameter(Long pageId,Long pageRuleId) {
		return ruleActionRepository.getActions(pageId,pageRuleId, JwtTokenDataHolder.getInstance().getPlatform());
	}
	
	@Override
	public List<RengineAction> getActions(Long pageId,Long pageRuleId) {
		return ruleActionRepository.findByPageIdAndPageRuleIdAndPlatform(pageId,pageRuleId, JwtTokenDataHolder.getInstance().getPlatform());
	}
	
	@Override
	public RengineAction getActionByActionName(String actionName) {
		return ruleActionRepository.findByActionNameAndPlatform(actionName, JwtTokenDataHolder.getInstance().getPlatform());
	}

	@Override
	public List<RengineAction> getActionsByPageId(Long pageId) {
		return ruleActionRepository.findByPageIdAndPlatform(pageId, JwtTokenDataHolder.getInstance().getPlatform());
	}
	
	@Override
	public RengineAction getActionByActionId(Long actionId) {
		Optional<RengineAction> result =  ruleActionRepository.findById(actionId);
		if(result.isPresent()) {
			return result.get();
		}
		return null;
	}

	@Override
	public List<RengineAction> getActionsByPageRuleId(Long pageRuleId) {
		return ruleActionRepository.findByPageRuleIdAndPlatform(pageRuleId, JwtTokenDataHolder.getInstance().getPlatform());
	}
}