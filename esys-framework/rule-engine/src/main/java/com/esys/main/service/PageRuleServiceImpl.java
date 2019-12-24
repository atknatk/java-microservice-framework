package com.esys.main.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.esys.main.controller.output.OutputDTO;
import com.esys.main.dao.ActionDao;
import com.esys.main.dao.PageDao;
import com.esys.main.dao.PageRuleDao;
import com.esys.main.dto.PageRuleDTO;
import com.esys.main.entity.RengineAction;
import com.esys.main.entity.RenginePage;
import com.esys.main.entity.RenginePageRule;
import com.esys.main.utils.JwtTokenDataHolder;

@Service("pageRuleService")
public class PageRuleServiceImpl implements PageRuleService {
	
	
	@Autowired
	private PageRuleDao pageRuleDao;
	
	@Autowired
	private PageDao pageDao;
	
	@Autowired
	private ActionDao actionDao;
	
	@Override
	public OutputDTO removeRule(PageRuleDTO input) {
		OutputDTO output = new OutputDTO();
		
		RenginePageRule ruleEntity =  pageRuleDao.getPageRuleByPageRuleId(input.getPageRuleId());
		if(ruleEntity == null) {
			output.addErrorMessage("Rule Bulunamadı");
			return output;
		}
		
		//rule bagli action varsa silinmeli
		List<RengineAction> actionList =  actionDao.getActionsByPageRuleId(ruleEntity.getPageRuleId());
		if(!CollectionUtils.isEmpty(actionList)) {
			for(RengineAction action : actionList) {
				actionDao.remove(action);	
			}	
		}
			
		pageRuleDao.remove(ruleEntity);
		updateOrderForRemove(ruleEntity);
		
		output.addSuccessMessage("Rule Deleted!...");
		return output;
	}
	
	private void updateOrderForRemove(RenginePageRule ruleEntity) {
		BigDecimal orderNo = ruleEntity.getOrderNo();
		List<RenginePageRule> pageRules = pageRuleDao.getPageRuleByGreaterThanOrderNoAndPageId(orderNo, ruleEntity.getPageId());
		for(RenginePageRule pageRule:pageRules) {
			BigDecimal newOrderNo = pageRule.getOrderNo();
			newOrderNo = newOrderNo.subtract(BigDecimal.ONE);
			
			pageRule.setOrderNo(newOrderNo);
			pageRuleDao.saveOrUpdate(pageRule);
		}
	}
	
	@Override
	public OutputDTO<Page<PageRuleDTO>> getPageRules(Long pageId,Pageable pagable){
		OutputDTO<Page<PageRuleDTO>> output = new OutputDTO<Page<PageRuleDTO>>();
		Page<RenginePageRule> pageRuleList = pageRuleDao.getPageRulesByPageId(pageId,pagable);
		output.addSuccessMessage("Sorgulama Basarili");
		output.setOutputData(PageRuleDTO.mapEntityPageIntoDTOPage(pageRuleList));
		
		return output;
	}
	
	@Override
	public OutputDTO<PageRuleDTO> updateRuleOrder(PageRuleDTO pageRule){
		OutputDTO<PageRuleDTO> output = new OutputDTO<PageRuleDTO>();
		Long pageId = pageRule.getPageId();
		Long pageRuleId = pageRule.getPageRuleId();
		
		RenginePageRule ruleEntity =  pageRuleDao.getPageRuleByPageRuleId(pageRuleId);
		if(ruleEntity == null) {
			output.addErrorMessage("Rule bulunamadi");
			return output;
		}
		BigDecimal orderNo = ruleEntity.getOrderNo();
		BigDecimal newOrderNo = pageRule.getOrderNo();
		RenginePageRule changeEntity = pageRuleDao.getPageRuleByOrderNoAndPageId(newOrderNo, pageId);
		if(changeEntity != null) {
			changeEntity.setOrderNo(orderNo);
			pageRuleDao.saveOrUpdate(changeEntity);
		}
		
		ruleEntity.setOrderNo(newOrderNo);
		ruleEntity = pageRuleDao.saveOrUpdate(ruleEntity); 

		output.addSuccessMessage("Sıra Guncellendi!");
		output.setOutputData(PageRuleDTO.toDTO(ruleEntity));
		return output;
	}

	@Override
	public OutputDTO<PageRuleDTO> updateRule(PageRuleDTO input) {
		OutputDTO<PageRuleDTO> output = new OutputDTO<PageRuleDTO>();
		
		RenginePageRule ruleEntity = pageRuleDao.getPageRuleByPageRuleId(input.getPageRuleId());
		if(ruleEntity == null) {
			output.addErrorMessage("Rule bulunamadi");
			return output;
		}
		
		
		if(!input.getRuleName().equals(ruleEntity.getRuleName())) {
			RenginePageRule rule2 = pageRuleDao.getPageRuleByRuleNameAndPageId(input.getRuleName(), ruleEntity.getPageId());
			if(rule2 != null) {
				output.addErrorMessage("Girilen isimde baska bir Rule bulunmakta!");
				return output;
			}
		}
		ruleEntity.setUpdateDate(new Date());
		ruleEntity.setRuleName(input.getRuleName());
		ruleEntity.setRuleText(input.getRuleText());
		ruleEntity = pageRuleDao.saveOrUpdate(ruleEntity);
		
		output.addSuccessMessage("Rule Update Sucsess...");
		output.setOutputData(PageRuleDTO.toDTO(ruleEntity));
		return output;
	}
	
	@Override
	public OutputDTO<PageRuleDTO> createRule(PageRuleDTO input) {
		
		OutputDTO<PageRuleDTO> output = new OutputDTO<PageRuleDTO>();
		
		String platform = JwtTokenDataHolder.getInstance().getPlatform();
		String pageName = input.getPageName();
		String ruleName = input.getRuleName();
		RenginePage page = pageDao.getPageByPageName(pageName);
		if(page == null) {
			output.addErrorMessage("sayfa bulunamadi");
			return output;
		}
		
		RenginePageRule pageRule = pageRuleDao.getPageRuleByRuleNameAndPageId(ruleName, page.getPageId());
		if(pageRule != null) {
			output.addErrorMessage("Bu isimde rule kaydi vardir baska bir isim giriniz");
			return output;
		}
		
		RenginePageRule ruleEntity = new RenginePageRule();
		ruleEntity.setCreateDate(new Date());
		BigDecimal maxOrderNo = pageRuleDao.findMaxOrderNo(page.getPageId());
		if (maxOrderNo == null) {
			maxOrderNo = new BigDecimal(0);
		}
		ruleEntity.setOrderNo(maxOrderNo.add(BigDecimal.ONE));
		ruleEntity.setPageId(page.getPageId());
		ruleEntity.setPlatform(platform);
		ruleEntity.setRuleName(ruleName);
		ruleEntity.setRuleText(input.getRuleText());
		ruleEntity = pageRuleDao.saveOrUpdate(ruleEntity);
		
		output.setOutputData(PageRuleDTO.toDTO(ruleEntity));	
		output.addSuccessMessage("Rule Create!");
		return output;
	}

	

}
