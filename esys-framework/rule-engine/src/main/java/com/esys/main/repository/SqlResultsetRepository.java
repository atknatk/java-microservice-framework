package com.esys.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.esys.main.entity.RengineSqlResultSet;

public interface SqlResultsetRepository extends JpaRepository<RengineSqlResultSet, Long>,JpaSpecificationExecutor<RengineSqlResultSet>{
	

	@Query(" select r from RengineSqlResultSet r" + 
			"  where (r.pageId = :pageId or r.pageId is null)" + 
			"   and (r.pageRuleId = :pageRuleId or r.pageRuleId is null)" + 
			"   and r.platform = :platform")
	List<RengineSqlResultSet> getSqlResultSet(@Param("pageId") Long pageId ,
			@Param("pageRuleId") Long pageRuleId,@Param("platform") String platform);
	
	RengineSqlResultSet findByResultSetNameAndPlatform(String resultSetName,String platform);
	
}
