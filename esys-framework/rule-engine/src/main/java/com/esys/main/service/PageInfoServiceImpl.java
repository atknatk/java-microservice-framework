package com.esys.main.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esys.main.controller.input.FieldData;
import com.esys.main.controller.input.ListData;
import com.esys.main.controller.input.PageInfoSaveInput;
import com.esys.main.dao.PageInfoDao;
import com.esys.main.entity.RenginePageInfo;
import com.esys.main.utils.JwtTokenDataHolder;

@Service("pageInfoServiceImpl")
public class PageInfoServiceImpl implements PageInfoService {
	
	
	@Autowired
	PageInfoDao pageInfoDao;
	
	@Override
	public void remove(RenginePageInfo renginePageInfo) {
		pageInfoDao.remove(renginePageInfo);
	}
	
	@Override
	public List<RenginePageInfo> getPageInfoByPageId(Long pageId){
		return pageInfoDao.getPageInfoByPageId(pageId);
	}
	
	@Override
	public List<RenginePageInfo> save(PageInfoSaveInput input) {
		List<RenginePageInfo> outputList = new ArrayList<>();
		
		String platform = JwtTokenDataHolder.getInstance().getPlatform();
		List<FieldData> fieldDataList = input.getFieldDataList();
		
		//validasyon yapılacak
		
	
		for(FieldData fieldData :fieldDataList) {
			String fieldName = fieldData.getFieldName();
			String defaultValue = fieldData.getDefaultValue();
			String dataType = fieldData.getType();
			List<ListData> listData = fieldData.getListData();
			if(listData != null && listData.size() > 0) {
				//list datalar burda ayrı bir tabloya page ile iliskili yazılacak
				
			}
			RenginePageInfo renginePageInfo = new RenginePageInfo();
			renginePageInfo.setCreateDate(new Date());
			renginePageInfo.setDefaultValue(defaultValue);
			renginePageInfo.setFieldName(fieldName);
			renginePageInfo.setPlatform(platform);
			renginePageInfo.setDataType(dataType);
			renginePageInfo = pageInfoDao.saveOrUpdate(renginePageInfo);
			outputList.add(renginePageInfo);
		}
		return outputList;
	}

}
