package com.esys.main.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.esys.main.entity.RenginePage;
import com.esys.main.repository.PageRepository;
import com.esys.main.utils.JwtTokenDataHolder;

@Repository
public class PageDaoImpl implements PageDao {
	
	
	@Autowired
	private PageRepository pageRepository;
	
	@Override
	public RenginePage saveOrUpdate(RenginePage renginePage) {
		return pageRepository.save(renginePage);
	}
	
	@Override
	public void remove(RenginePage renginePage) {
		pageRepository.delete(renginePage);
	}

	@Override
	public Page<RenginePage> getPages(Pageable page) {
		return pageRepository.findByPlatform(JwtTokenDataHolder.getInstance().getPlatform(),page);
	}

	@Override
	public RenginePage getPageByPageId(Long pageId) {
		Optional<RenginePage> page = pageRepository.findById(pageId);
		if(page != null) {
			return page.get();
		}
		return null;
	}
	
	@Override
	public RenginePage getPageByPageName(String pageName) {
		return pageRepository.findByPageNameAndPlatform(pageName,JwtTokenDataHolder.getInstance().getPlatform());
	}
}