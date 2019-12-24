package com.esys.main.dao;

import java.util.List;

import com.esys.main.entity.RenginePageInfo;

public interface PageInfoDao {

	RenginePageInfo saveOrUpdate(RenginePageInfo RenginePageInfo);

	void remove(RenginePageInfo RenginePageInfo);

	RenginePageInfo getFiledNameAndPageId(String fieldName,Long pageId);

	List<RenginePageInfo> getPageInfoByPageId(Long pageId);

}
