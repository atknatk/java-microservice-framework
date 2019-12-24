package com.esys.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.esys.main.entity.RenginePageInfo;

public interface PageInfoRepository extends JpaRepository<RenginePageInfo, Long>,JpaSpecificationExecutor<RenginePageInfo>{
	

	RenginePageInfo findByFieldNameAndPageId(String fieldName, Long pageId);

	List<RenginePageInfo> findByPageId(Long pageId);

}
