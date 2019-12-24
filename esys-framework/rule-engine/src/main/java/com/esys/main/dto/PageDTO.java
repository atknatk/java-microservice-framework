package com.esys.main.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.CollectionUtils;

import com.esys.main.entity.RenginePage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO{

	private Long pageId;
	private String pageName;
	private List<PageRuleDTO> ruleList;
	private Date createDate;
	private Date updateDate;
	private String platform;

	public RenginePage toEntity() {

		RenginePage entity = new RenginePage();
		entity.setPageId(this.getPageId());
		entity.setCreateDate(this.getCreateDate());
		entity.setPageName(this.getPageName());
		entity.setPlatform(this.getPlatform());
		entity.setUpdateDate(this.getUpdateDate());
		if(!CollectionUtils.isEmpty(this.getRuleList())) {
			entity.setRuleList(PageRuleDTO.toEntityList(this.getRuleList()));
		}
		
		return entity;
	}

	public static RenginePage toEntity(PageDTO input) {
		RenginePage entity = new RenginePage();
		entity.setPageId(input.getPageId());
		entity.setCreateDate(input.getCreateDate());
		entity.setPageName(input.getPageName());
		entity.setPlatform(input.getPlatform());
		entity.setUpdateDate(input.getUpdateDate());
		if(!CollectionUtils.isEmpty(input.getRuleList())) {
			entity.setRuleList(PageRuleDTO.toEntityList(input.getRuleList()));
		}
		return entity;
	}

	public static PageDTO toDTO(final RenginePage entity) {

		PageDTO dto = new PageDTO();
		dto.setPageId(entity.getPageId());
		dto.setCreateDate(entity.getCreateDate());
		dto.setPageName(entity.getPageName());
		dto.setPlatform(entity.getPlatform());
		dto.setUpdateDate(entity.getUpdateDate());
		if(!CollectionUtils.isEmpty(entity.getRuleList())) {
			dto.setRuleList(PageRuleDTO.toDTOList(entity.getRuleList()));
		}
		return dto;
	}

	public static List<PageDTO> toDTOList(final List<RenginePage> entityList) {
		List<PageDTO> dtoList = new ArrayList<PageDTO>(0);
		if (entityList != null) {
			for (RenginePage entity : entityList) {
				PageDTO dto = PageDTO.toDTO(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}

	public static List<RenginePage> toEntityList(final List<PageDTO> dtoList) {
		List<RenginePage> entityList = new ArrayList<RenginePage>(0);
		if (entityList != null) {
			for (PageDTO dto : dtoList) {
				RenginePage entity = dto.toEntity();
				entityList.add(entity);
			}
		}
		return entityList;
	}

	public static Page<PageDTO> mapEntityPageIntoDTOPage(Page<RenginePage> source) {
		if (source != null) {
			if (!source.getContent().isEmpty()) {
				return source.map(PageDTO::toDTO);
			} else {
				List emptyList = new ArrayList<PageDTO>();
				Page<PageDTO> pages = new PageImpl<>(emptyList);
				return pages;
			}
		} else {
			List emptyList = new ArrayList<PageDTO>();
			Page<PageDTO> pages = new PageImpl<>(emptyList);
			return pages;

		}
	}
	
}
