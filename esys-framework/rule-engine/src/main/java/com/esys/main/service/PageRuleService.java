package com.esys.main.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.esys.main.controller.output.OutputDTO;
import com.esys.main.dto.PageRuleDTO;

public interface PageRuleService {
	
	OutputDTO<PageRuleDTO> updateRuleOrder(PageRuleDTO pageRule);

	OutputDTO<PageRuleDTO> createRule(PageRuleDTO input);

	OutputDTO removeRule(PageRuleDTO input);

	OutputDTO<PageRuleDTO> updateRule(PageRuleDTO input);

	OutputDTO<Page<PageRuleDTO>> getPageRules(Long pageId, Pageable pagable);
	
}
