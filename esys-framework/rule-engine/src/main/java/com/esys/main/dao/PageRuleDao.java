package com.esys.main.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.esys.main.entity.RenginePageRule;

public interface PageRuleDao {

	RenginePageRule saveOrUpdate(RenginePageRule renginePageRule);

	BigDecimal findMaxOrderNo(Long pageId);

	RenginePageRule getPageRuleByRuleNameAndPageId(String ruleName, Long pageId);
	
	RenginePageRule getPageRuleByPageRuleIdAndPageId(Long pageRuleId, Long pageId);
	
	RenginePageRule getPageRuleByPageRuleId(Long ruleId);

	RenginePageRule getPageRuleByOrderNoAndPageId(BigDecimal orderNo, Long pageId);

	void remove(RenginePageRule ruleEntity);

	List<RenginePageRule> getPageRuleByGreaterThanOrderNoAndPageId(BigDecimal orderNo, Long pageId);

	Page<RenginePageRule> getPageRulesByPageId(Long pageId, Pageable pagable);

	List<RenginePageRule> getPageRuleByOrderBy(Long pageId);
}
