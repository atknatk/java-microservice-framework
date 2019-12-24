package com.esys.main.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.esys.main.entity.RenginePageInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageInfoDTO{

	private Long pageInfoId;
	private String fieldName;
	private Long pageId;
	private String dataType;
	private String defaultValue;
	private Date createDate;
	private Date updateDate;
	private String platform;

	public RenginePageInfo toEntity() {

		RenginePageInfo entity = new RenginePageInfo();
		entity.setCreateDate(this.getCreateDate());
		entity.setDataType(this.getDataType());
		entity.setDefaultValue(this.getDefaultValue());
		entity.setFieldName(this.getFieldName());
		entity.setPageInfoId(this.getPageInfoId());
		entity.setPageId(this.getPageId());
		entity.setPlatform(this.getPlatform());
		entity.setUpdateDate(this.getUpdateDate());
		return entity;
	}

	public static RenginePageInfo toEntity(PageInfoDTO input) {
		RenginePageInfo entity = new RenginePageInfo();
		entity.setCreateDate(input.getCreateDate());
		entity.setDataType(input.getDataType());
		entity.setDefaultValue(input.getDefaultValue());
		entity.setFieldName(input.getFieldName());
		entity.setPageInfoId(input.getPageInfoId());
		entity.setPageId(input.getPageId());
		entity.setPlatform(input.getPlatform());
		entity.setUpdateDate(input.getUpdateDate());
		return entity;
	}

	public static PageInfoDTO toDTO(final RenginePageInfo entity) {

		PageInfoDTO dto = new PageInfoDTO();
		dto.setCreateDate(entity.getCreateDate());
		dto.setDataType(entity.getDataType());
		dto.setDefaultValue(entity.getDefaultValue());
		dto.setFieldName(entity.getFieldName());
		dto.setPageInfoId(entity.getPageInfoId());
		dto.setPageId(entity.getPageId());
		dto.setPlatform(entity.getPlatform());
		dto.setUpdateDate(entity.getUpdateDate());
		return dto;
	}

	public static List<PageInfoDTO> toDTOList(final List<RenginePageInfo> entityList) {
		List<PageInfoDTO> dtoList = new ArrayList<PageInfoDTO>(0);
		if (entityList != null) {
			for (RenginePageInfo entity : entityList) {
				PageInfoDTO dto = PageInfoDTO.toDTO(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}

	public static List<RenginePageInfo> toEntityList(final List<PageInfoDTO> dtoList) {
		List<RenginePageInfo> entityList = new ArrayList<RenginePageInfo>(0);
		if (entityList != null) {
			for (PageInfoDTO dto : dtoList) {
				RenginePageInfo entity = dto.toEntity();
				entityList.add(entity);
			}
		}
		return entityList;
	}

}
