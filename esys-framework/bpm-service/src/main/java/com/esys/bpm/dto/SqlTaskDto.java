package com.esys.bpm.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.esys.bpm.entity.Process;
import com.esys.bpm.entity.SqlTask;
import com.esys.bpm.enums.ProcessComponent;
import com.esys.bpm.enums.SqlType;
import com.esys.bpm.utils.CommonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)

@XmlRootElement(name = "sqlTask")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class SqlTaskDto extends TaskDto {

	@XmlTransient
	private ProcessComponent component = ProcessComponent.SQL_TASK;

	@XmlTransient
	private Long sqlTaskId;

	@XmlTransient
	private SqlType sqlType;
	@XmlTransient
	private String functionName;
	@XmlTransient
	private String nativeSql;

	public SqlTask toEntity(Process process) {

		SqlTask entity = toEntity();

		entity.setProcess(process);

		return entity;
	}

	public SqlTask toEntity() {

		SqlTask entity = new SqlTask();

		entity.setSqlTaskId(CommonUtil.parseIdFromXmlId(this.getXmlId()));
		entity.setXmlId(CommonUtil.parseOriginalXmlIdFromXmlId(this.getXmlId()));

		entity.setName(this.getName());
		entity.setDescription(this.getDescription());

		entity.setSqlType(this.getSqlType());
		entity.setFunctionName(this.getFunctionName());
		entity.setNativeSql(this.getNativeSql());

		return entity;
	}

	public static SqlTaskDto toDto(final SqlTask entity) {

		SqlTaskDto dto = new SqlTaskDto();
		dto.setSqlTaskId(entity.getSqlTaskId());
		dto.setXmlId(entity.getSqlTaskId().toString() + "_" + entity.getXmlId());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());

		dto.setSqlType(entity.getSqlType());
		dto.setFunctionName(entity.getFunctionName());
		dto.setNativeSql(entity.getNativeSql());

		return dto;
	}

	public static List<SqlTask> toEntityList(final List<SqlTaskDto> dtoList, Process process) {
		List<SqlTask> entityList = new ArrayList<SqlTask>(0);
		if (dtoList != null) {
			for (SqlTaskDto dto : dtoList) {
				SqlTask entity = dto.toEntity(process);
				entityList.add(entity);
			}
		}
		return entityList;
	}

	public static List<SqlTaskDto> toDtoList(final List<SqlTask> entityList) {
		List<SqlTaskDto> dtoList = new ArrayList<SqlTaskDto>(0);
		if (entityList != null) {
			for (SqlTask entity : entityList) {
				SqlTaskDto dto = SqlTaskDto.toDto(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
}
