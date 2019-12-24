package com.esys.main.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.esys.main.entity.RenginePageRule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRuleDTO {

	private Long pageRuleId;
	private Long pageId;
	private String pageName;
	private String ruleName;
	private String ruleText;
	private BigDecimal orderNo;
	private Date createDate;
	private Date updateDate;
	private String platform;

	public RenginePageRule toEntity() {

		RenginePageRule entity = new RenginePageRule();
		entity.setCreateDate(this.getCreateDate());
		entity.setOrderNo(this.getOrderNo());
		entity.setPageId(this.getPageId());
		entity.setPageRuleId(this.getPageRuleId());
		entity.setPlatform(this.getPlatform());
		entity.setRuleName(this.getRuleName());
		entity.setRuleText(this.getRuleText());
		entity.setUpdateDate(this.getUpdateDate());
		return entity;
	}

	public static RenginePageRule toEntity(PageRuleDTO input) {
		RenginePageRule entity = new RenginePageRule();
		entity.setCreateDate(input.getCreateDate());
		entity.setOrderNo(input.getOrderNo());
		entity.setPageId(input.getPageId());
		entity.setPageRuleId(input.getPageRuleId());
		entity.setPlatform(input.getPlatform());
		entity.setRuleName(input.getRuleName());
		entity.setRuleText(input.getRuleText());
		entity.setUpdateDate(input.getUpdateDate());
		return entity;
	}

	public static PageRuleDTO toDTO(final RenginePageRule entity) {

		PageRuleDTO dto = new PageRuleDTO();
		dto.setCreateDate(entity.getCreateDate());
		dto.setOrderNo(entity.getOrderNo());
		dto.setPageId(entity.getPageId());
		dto.setPageRuleId(entity.getPageRuleId());
		dto.setPlatform(entity.getPlatform());
		dto.setRuleName(entity.getRuleName());
		dto.setRuleText(entity.getRuleText());
		dto.setUpdateDate(entity.getUpdateDate());
		return dto;
	}

	public static List<PageRuleDTO> toDTOList(final List<RenginePageRule> entityList) {
		List<PageRuleDTO> dtoList = new ArrayList<PageRuleDTO>(0);
		if (entityList != null) {
			for (RenginePageRule entity : entityList) {
				PageRuleDTO dto = PageRuleDTO.toDTO(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}

	public static List<RenginePageRule> toEntityList(final List<PageRuleDTO> dtoList) {
		List<RenginePageRule> entityList = new ArrayList<RenginePageRule>(0);
		if (entityList != null) {
			for (PageRuleDTO dto : dtoList) {
				RenginePageRule entity = dto.toEntity();
				entityList.add(entity);
			}
		}
		return entityList;
	}
	
	public static Page<PageRuleDTO> mapEntityPageIntoDTOPage(Page<RenginePageRule> source) {
		if (source != null) {
			if (!source.getContent().isEmpty()) {
				return source.map(PageRuleDTO::toDTO);
			} else {
				List emptyList = new ArrayList<PageRuleDTO>();
				Page<PageRuleDTO> pages = new PageImpl<>(emptyList);
				return pages;
			}
		} else {
			List emptyList = new ArrayList<PageRuleDTO>();
			Page<PageRuleDTO> pages = new PageImpl<>(emptyList);
			return pages;

		}
	}

}
