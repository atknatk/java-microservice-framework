package com.esys.main.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esys.main.controller.input.action.UpdateActionInput;
import com.esys.main.controller.output.OutputDTO;
import com.esys.main.dao.ActionDao;
import com.esys.main.dao.PageDao;
import com.esys.main.dao.PageRuleDao;
import com.esys.main.dto.ActionDTO;
import com.esys.main.entity.RengineAction;
import com.esys.main.entity.RenginePage;
import com.esys.main.entity.RenginePageRule;
import com.esys.main.utils.JwtTokenDataHolder;
import com.esys.main.utils.StringUtil;

@Service("actionService")
public class ActionServiceImpl implements ActionService {

	@Autowired
	private ActionDao actionDao;
	
	@Autowired
	private PageRuleDao pageRuleDao;
	
	@Autowired
	private PageDao pageDao;
	
	@Override
	public OutputDTO remove(Long actionId) {
		OutputDTO output = new OutputDTO();
		
		RengineAction action = actionDao.getActionByActionId(actionId);
		if(action == null) {
			output.addErrorMessage("Action bulunamadi");
			return output;
		}
		
		actionDao.remove(action);                                                                                                                                                                                                                                               
		output.addSuccessMessage("Action Deleted!...");
		return output;
	}

	@Override
	public OutputDTO<List<ActionDTO>> getActionList(String pageName,String ruleName) {
		OutputDTO<List<ActionDTO>> output = new OutputDTO<List<ActionDTO>>();
		
		OutputDTO<ActionDTO> validActionDTO = generateValidId(pageName,ruleName);
		if(validActionDTO.isThereMessage()) {
			output.setMessages(validActionDTO.getMessages());
			return output;
		}
		ActionDTO actionDTO  = validActionDTO.getOutputData();
		Long pageId = actionDTO.getPageId();
		Long pageRuleId = actionDTO.getPageRuleId();
		
		List<RengineAction> actionList =  actionDao.getActionsWithNullParameter(pageId,pageRuleId);
		output.setOutputData(ActionDTO.toDTOList(actionList));
		output.addSuccessMessage("Sorgu Basarili");
		return output;
	}
	
	@Override
	public OutputDTO<ActionDTO> updateAction(UpdateActionInput input) {
		OutputDTO<ActionDTO> output = new OutputDTO<ActionDTO>();
		RengineAction action = actionDao.getActionByActionId(input.getActionId());
		if(action == null) {
			output.addErrorMessage("Action bulunamadi");
			return output;
		}
		
		
		if(!input.getActionName().equals(action.getActionName())) {
			RengineAction action2 = actionDao.getActionByActionName(input.getActionName());
			if(action2 != null) {
				output.addErrorMessage("Girilen isimde baska bir action bulunmakta!");
				return output;
			}
		}
		
		String pageName = input.getPageName();
		String ruleName = input.getRuleName();
		
		OutputDTO<ActionDTO> validActionDTO = generateValidId(pageName,ruleName);
		if(validActionDTO.isThereMessage()) {
			return validActionDTO;
		}
		ActionDTO actionDTO  = validActionDTO.getOutputData();
		Long pageId = actionDTO.getPageId();
		Long pageRuleId = actionDTO.getPageRuleId();
		action.setActionName(input.getActionName());
		action.setActionBody(input.getActionBody());
		action.setActionType(input.getActionType());
		action.setContentType(input.getContentType());	
		action.setHttpMethod(input.getHttpMethod());
		action.setUrl(input.getUrl());
		action.setPageRuleId(pageRuleId);
		action.setPageId(pageId);
		action.setUpdateDate(new Date());
		action = actionDao.saveOrUpdate(action); 

		output.addSuccessMessage("Action Update Sucsess...");
		output.setOutputData(ActionDTO.toDTO(action));
		return output;
		
	}

	@Override
	public OutputDTO<ActionDTO> createAction(ActionDTO input) {
		OutputDTO<ActionDTO> output = new OutputDTO<ActionDTO>();
		RengineAction action = actionDao.getActionByActionName(input.getActionName());
		if(action != null) {
			output.addErrorMessage("Bu isimde Action mevcutdur lutfen guncelleme servisini kullanin!");
			return output;
		}
		String pageName = input.getPageName();
		String ruleName = input.getRuleName();
		OutputDTO<ActionDTO> validActionDTO = generateValidId(pageName,ruleName);
		if(validActionDTO.isThereMessage()) {
			return validActionDTO;
		}
		ActionDTO actionDTO  = validActionDTO.getOutputData();
		Long pageId = actionDTO.getPageId();
		Long pageRuleId = actionDTO.getPageRuleId();
		
		RengineAction actionEntity = new RengineAction();
		actionEntity.setActionBody(input.getActionBody());
		actionEntity.setActionName(input.getActionName());
		actionEntity.setActionType(input.getActionType());
		actionEntity.setContentType(input.getContentType());	
		actionEntity.setHttpMethod(input.getHttpMethod());
		actionEntity.setUrl(input.getUrl());
		actionEntity.setPageRuleId(pageRuleId);
		actionEntity.setPageId(pageId);
		actionEntity.setPlatform(JwtTokenDataHolder.getInstance().getPlatform());
		actionEntity.setCreateDate(new Date());
		actionEntity.setSeverity(input.getSeverity());
		actionEntity = actionDao.saveOrUpdate(actionEntity); 

		output.addSuccessMessage("Action Crate Sucsess...");
		output.setOutputData(ActionDTO.toDTO(actionEntity));
		return output;
	}
	
	private OutputDTO<ActionDTO> generateValidId(String pageName,String ruleName){
		Long pageId = null;
		Long pageRuleId = null;
		OutputDTO<ActionDTO> output = new OutputDTO<ActionDTO>();
		if(StringUtil.isNotEmpty(pageName)) {
			RenginePage page = pageDao.getPageByPageName(pageName);
			if(page == null) {
				output.addErrorMessage("Sayfa bulunamadı");
				return output;
			}else {
				pageId = page.getPageId();
			}
		}
		if(StringUtil.isNotEmpty(ruleName)) {
			if(pageId == null) {
				output.addErrorMessage("Geçerli Sayfa ismi girmelisiniz!..");
				return output;
			}else {
				RenginePageRule ruleEntity =  pageRuleDao.getPageRuleByRuleNameAndPageId(ruleName,pageId);	
				if(ruleEntity == null) {
					output.addErrorMessage("rule bulunamadi");
					return output;
				}else {
					pageRuleId = ruleEntity.getPageRuleId();
				}
			}
		}
		ActionDTO actionDTO = new ActionDTO();
		actionDTO.setPageId(pageId);
		actionDTO.setPageRuleId(pageRuleId);
		output.setOutputData(actionDTO);
		
		return output;
		
	}

}
