package com.esys.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.esys.main.entity.RengineAction;

public interface ActionRepository extends JpaRepository<RengineAction, Long>,JpaSpecificationExecutor<RengineAction>{

	@Query(" select r from RengineAction r" + 
			"  where (r.pageId = :pageId or r.pageId is null)" + 
			"   and (r.pageRuleId = :pageRuleId or r.pageRuleId is null)" + 
			"   and r.platform = :platform")
	List<RengineAction> getActions(@Param("pageId") Long pageId ,
			@Param("pageRuleId") Long pageRuleId,@Param("platform") String platform);
	
	List<RengineAction> findByPageIdAndPageRuleIdAndPlatform(Long pageId ,Long pageRuleId,String platform);
	
	List<RengineAction> findByPageIdAndPlatform(Long pageId ,String platform);
	
	List<RengineAction> findByPageRuleIdAndPlatform(Long pageRuleId ,String platform);

	RengineAction findByActionNameAndPlatform(String actionName, String platform);

}
