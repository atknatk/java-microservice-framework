package com.esys.main.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.esys.main.entity.RenginePage;

public interface PageDao {

	void remove(RenginePage renginePage);

	RenginePage saveOrUpdate(RenginePage renginePage);

	Page<RenginePage> getPages(Pageable pageable);

	RenginePage getPageByPageId(Long pageId);

	RenginePage getPageByPageName(String pageName);

}
