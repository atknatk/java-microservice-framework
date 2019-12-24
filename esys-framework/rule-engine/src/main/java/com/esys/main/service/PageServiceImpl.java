package com.esys.main.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.esys.main.controller.output.OutputDTO;
import com.esys.main.dao.ActionDao;
import com.esys.main.dao.PageDao;
import com.esys.main.dto.PageDTO;
import com.esys.main.entity.RengineAction;
import com.esys.main.entity.RenginePage;
import com.esys.main.utils.JwtTokenDataHolder;
import com.esys.main.utils.StringUtil;

@Service("pageService")
public class PageServiceImpl implements PageService {
	
	
	@Autowired
	private PageDao pageDao;
	
	@Autowired
	private ActionDao actionDao;
	
	@Override
	public OutputDTO<PageDTO> createPage(PageDTO input) {
		OutputDTO<PageDTO> output = new OutputDTO<PageDTO>();
		String message = validInput(input);
		if(!StringUtil.isEmpty(message)) {
			output.addErrorMessage(message);
			return output;
		}
		RenginePage page = pageDao.getPageByPageName(input.getPageName());
		if(page != null) {
			output.addErrorMessage("Bu isimde page mevcut lutfen baska isim giriniz!..");
			return output;
		}
		
		RenginePage entity = new RenginePage();
		entity.setPageName(input.getPageName());
		entity.setCreateDate(new Date());
		entity.setPlatform(JwtTokenDataHolder.getInstance().getPlatform());
		entity = pageDao.saveOrUpdate(entity);
		
		output.addSuccessMessage("Page Create Sucsess...");
		output.setOutputData(PageDTO.toDTO(entity));
		return output;
	}
	
	private String validInput(PageDTO input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputDTO removePage(PageDTO input) {
		OutputDTO output = new OutputDTO();
		
		RenginePage page = pageDao.getPageByPageId(input.getPageId());
		if(page == null) {
			output.addErrorMessage("sayfa bulunamadi");
			return output;
		}
		
		//sayfaya bagli action varsa silinmeli
		List<RengineAction> actionList =  actionDao.getActionsByPageId(page.getPageId());
		for(RengineAction action : actionList) {
			actionDao.remove(action);	
		}
		
		pageDao.remove(page);                                                                                                                                                                                                                                             
		output.addSuccessMessage("Page Deleted!...");
		return output;
	}
	
	@Override
	public OutputDTO<PageDTO> updatePage(PageDTO input) {
		OutputDTO<PageDTO> output = new OutputDTO<PageDTO>();
		RenginePage page = pageDao.getPageByPageId(input.getPageId());
		if(page == null) {
			output.addErrorMessage("sayfa bulunamadi");
			return output;
		}
		
		
		if(!input.getPageName().equals(page.getPageName())) {
			RenginePage page2 = pageDao.getPageByPageName(input.getPageName());
			if(page2 != null) {
				output.addErrorMessage("Girilen isimde baska bir sayfa bulunmakta!");
				return output;
			}
		}
		page.setPageName(input.getPageName());
		page.setUpdateDate(new Date());
		page = pageDao.saveOrUpdate(page);
		
		output.addSuccessMessage("Page Update Sucsess...");
		output.setOutputData(PageDTO.toDTO(page));
		return output;
	}
	
	@Override
	public OutputDTO<Page<PageDTO>> getPages(Pageable page){
		OutputDTO<Page<PageDTO>> output = new OutputDTO<Page<PageDTO>>();
		Page<RenginePage> pageList = pageDao.getPages(page);                                                                                                                                                                                                                                           
		output.addSuccessMessage("Sorgu Basarili");
		output.setOutputData(PageDTO.mapEntityPageIntoDTOPage(pageList));
		return output;
	}

}
