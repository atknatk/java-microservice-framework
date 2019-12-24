package com.esys.main.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.esys.main.entity.RenginePageRule;
import com.esys.main.repository.PageRuleRepository;
import com.esys.main.utils.JwtTokenDataHolder;

@Repository
public class PageRuleDaoImpl implements PageRuleDao {
	
	
	@Autowired
	private PageRuleRepository pageRuleRepository;
	
	public RenginePageRule saveOrUpdate(RenginePageRule renginePageRule) {
		return pageRuleRepository.save(renginePageRule);
	}
	
	public void remove(RenginePageRule renginePageRule) {
		pageRuleRepository.delete(renginePageRule);
	}
	
	public BigDecimal findMaxOrderNo(Long pageId) {
		return pageRuleRepository.findMaxOrderNo(pageId);
	}

	@Override
	public RenginePageRule getPageRuleByRuleNameAndPageId(String ruleName,Long pageId) {
		return pageRuleRepository.findByRuleNameAndPageId(ruleName,pageId);
	}
	
	@Override
	public RenginePageRule getPageRuleByOrderNoAndPageId(BigDecimal orderNo,Long pageId) {
		return pageRuleRepository.findByOrderNoAndPageIdAndPlatform(orderNo,pageId, JwtTokenDataHolder.getInstance().getPlatform());
	}
	
	@Override
	public List<RenginePageRule> getPageRuleByGreaterThanOrderNoAndPageId(BigDecimal orderNo,Long pageId) {
		return pageRuleRepository.findByOrderNoGreaterThanAndPageIdAndPlatform(orderNo,pageId, JwtTokenDataHolder.getInstance().getPlatform());
	}
	
	@Override
	public Page<RenginePageRule> getPageRulesByPageId(Long pageId,Pageable pagable) {
		return pageRuleRepository.findByPageIdAndPlatform(pageId, JwtTokenDataHolder.getInstance().getPlatform(),pagable);
	}

	@Override
	public RenginePageRule getPageRuleByPageRuleId(Long ruleId) {
		Optional<RenginePageRule> result =  pageRuleRepository.findById(ruleId);
		if(result.isPresent()) {
			return result.get();
		}
		return null;
	}

	@Override
	public RenginePageRule getPageRuleByPageRuleIdAndPageId(Long pageRuleId, Long pageId) {
		return pageRuleRepository.findByPageRuleIdAndPageId(pageRuleId,pageId);
	}
	
	@Override
	public List<RenginePageRule> getPageRuleByOrderBy(Long pageId) {
		return pageRuleRepository.findByPageIdAndPlatformOrderByOrderNoAsc(pageId,JwtTokenDataHolder.getInstance().getPlatform());
	}
}