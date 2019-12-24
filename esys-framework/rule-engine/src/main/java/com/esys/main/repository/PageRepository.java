package com.esys.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.esys.main.entity.RenginePage;

public interface PageRepository extends JpaRepository<RenginePage, Long>,JpaSpecificationExecutor<RenginePage>{

	Page<RenginePage> findByPlatform(String platform,Pageable page);
	
	RenginePage findByPageIdAndPlatform(Long pageId,String platform);
	
	RenginePage findByPageNameAndPlatform(String pageName,String platform);
}
