package com.esys.main.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.esys.main.entity.RenginePageRule;

public interface PageRuleRepository extends JpaRepository<RenginePageRule, Long>,JpaSpecificationExecutor<RenginePageRule>{
	
	
	RenginePageRule findByRuleNameAndPageId(String ruleName,Long pageId);
	
	RenginePageRule findByPageRuleIdAndPageId(Long pageRuleId,Long pageId);

	@Query("SELECT max(o.orderNo) FROM RenginePageRule o where o.pageId = :pageId")
	BigDecimal findMaxOrderNo(@Param("pageId") Long pageId);

	RenginePageRule findByOrderNoAndPageIdAndPlatform(BigDecimal orderNo, Long pageId, String platform);
	
	List<RenginePageRule> findByOrderNoGreaterThanAndPageIdAndPlatform(BigDecimal orderNo, Long pageId, String platform);

	Page<RenginePageRule> findByPageIdAndPlatform(Long pageId, String platform,Pageable page);

	List<RenginePageRule> findByPageIdAndPlatformOrderByOrderNoAsc(Long pageId, String platform);

}
