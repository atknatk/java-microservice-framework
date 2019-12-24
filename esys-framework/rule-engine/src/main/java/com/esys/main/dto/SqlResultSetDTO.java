 package com.esys.main.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.esys.main.entity.RengineSqlResultSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SqlResultSetDTO {
	 		
	private Long sqlId;
	private Long pageRuleId;
	private Long pageId;
	private String sql;
	private String resultSetName;
	private Date createDate;
	private Date updateDate;
	private String platform;
	private String pageName;
	private String ruleName;

	public RengineSqlResultSet toEntity() {

		RengineSqlResultSet entity = new RengineSqlResultSet();
		entity.setCreateDate(this.getCreateDate());
		entity.setPageId(this.getPageId());
		entity.setPageRuleId(this.getPageRuleId());
		entity.setPlatform(this.getPlatform());
		entity.setUpdateDate(this.getUpdateDate());
		entity.setSql(this.getSql());
		entity.setResultSetName(this.getResultSetName());
		entity.setSqlId(this.getSqlId());
		return entity;
	}

	public static RengineSqlResultSet toEntity(SqlResultSetDTO input) {
		RengineSqlResultSet entity = new RengineSqlResultSet();
		entity.setCreateDate(input.getCreateDate());
		entity.setPageId(input.getPageId());
		entity.setPageRuleId(input.getPageRuleId());
		entity.setPlatform(input.getPlatform());
		entity.setUpdateDate(input.getUpdateDate());
		entity.setSql(input.getSql());
		entity.setResultSetName(input.getResultSetName());
		entity.setSqlId(input.getSqlId());
		return entity;
	}

	public static SqlResultSetDTO toDTO(final RengineSqlResultSet entity) {

		SqlResultSetDTO dto = new SqlResultSetDTO();
		dto.setCreateDate(entity.getCreateDate());
		dto.setPageId(entity.getPageId());
		dto.setPageRuleId(entity.getPageRuleId());
		dto.setPlatform(entity.getPlatform());
		dto.setUpdateDate(entity.getUpdateDate());
		dto.setSql(entity.getSql());
		dto.setResultSetName(entity.getResultSetName());
		dto.setSqlId(entity.getSqlId());
		return dto;
	}

	public static List<SqlResultSetDTO> toDTOList(final List<RengineSqlResultSet> entityList) {
		List<SqlResultSetDTO> dtoList = new ArrayList<SqlResultSetDTO>(0);
		if (entityList != null) {
			for (RengineSqlResultSet entity : entityList) {
				SqlResultSetDTO dto = SqlResultSetDTO.toDTO(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}

	public static List<RengineSqlResultSet> toEntityList(final List<SqlResultSetDTO> dtoList) {
		List<RengineSqlResultSet> entityList = new ArrayList<RengineSqlResultSet>(0);
		if (entityList != null) {
			for (SqlResultSetDTO dto : dtoList) {
				RengineSqlResultSet entity = dto.toEntity();
				entityList.add(entity);
			}
		}
		return entityList;
	}
	
	public static Page<SqlResultSetDTO> mapEntityPageIntoDTOPage(Page<RengineSqlResultSet> source) {
		if (source != null) {
			if (!source.getContent().isEmpty()) {
				return source.map(SqlResultSetDTO::toDTO);
			} else {
				List emptyList = new ArrayList<SqlResultSetDTO>();
				Page<SqlResultSetDTO> pages = new PageImpl<>(emptyList);
				return pages;
			}
		} else {
			List emptyList = new ArrayList<SqlResultSetDTO>();
			Page<SqlResultSetDTO> pages = new PageImpl<>(emptyList);
			return pages;

		}
	}


}
