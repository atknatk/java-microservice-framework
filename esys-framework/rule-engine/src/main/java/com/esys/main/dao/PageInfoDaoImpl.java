package com.esys.main.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.esys.main.entity.RenginePageInfo;
import com.esys.main.repository.PageInfoRepository;

@Repository
public class PageInfoDaoImpl implements PageInfoDao {
	
	
	@Autowired
	private PageInfoRepository pageInfoRepository;
	
	@Override
	public RenginePageInfo saveOrUpdate(RenginePageInfo RenginePageInfo) {
		return pageInfoRepository.save(RenginePageInfo);
	}
	
	@Override
	public void remove(RenginePageInfo RenginePageInfo) {
		pageInfoRepository.delete(RenginePageInfo);
	}
	
	@Override
	public RenginePageInfo getFiledNameAndPageId(String filedName,Long pageId) {
		return pageInfoRepository.findByFieldNameAndPageId(filedName, pageId);
	}

	@Override
	public List<RenginePageInfo> getPageInfoByPageId(Long pageId) {
		return pageInfoRepository.findByPageId(pageId);
	}
}