package com.esys.main.service;

import java.util.List;

import com.esys.main.controller.input.PageInfoSaveInput;
import com.esys.main.entity.RenginePageInfo;

public interface PageInfoService {
	
	void remove(RenginePageInfo renginePageInfo);
	
	List<RenginePageInfo> save(PageInfoSaveInput pageInfoSaveInput);

	List<RenginePageInfo> getPageInfoByPageId(Long pageId);
	
}
