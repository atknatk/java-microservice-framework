package com.esys.bpm.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.esys.bpm.entity.BusinessRuleTask;
import com.esys.bpm.entity.Process;
import com.esys.bpm.enums.ProcessComponent;
import com.esys.bpm.utils.CommonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)

@XmlRootElement(name = "businessRuleTask")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class BusinessRuleTaskDto extends TaskDto {

	@XmlTransient
	private ProcessComponent component = ProcessComponent.BUSINESS_RULE_TASK;

	@XmlTransient
	private Long businessRuleTaskId;

	@XmlTransient
	private Long ruleId;

	public BusinessRuleTask toEntity(Process process) {

		BusinessRuleTask entity = toEntity();
		entity.setProcess(process);

		return entity;
	}

	public BusinessRuleTask toEntity() {

		BusinessRuleTask entity = new BusinessRuleTask();

		entity.setBusinessRuleTaskId(CommonUtil.parseIdFromXmlId(this.getXmlId()));
		entity.setXmlId(CommonUtil.parseOriginalXmlIdFromXmlId(this.getXmlId()));

		entity.setName(this.getName());
		entity.setDescription(this.getDescription());

		entity.setRuleId(this.getRuleId());

		return entity;
	}

	public static BusinessRuleTaskDto toDto(final BusinessRuleTask entity) {
		BusinessRuleTaskDto dto = new BusinessRuleTaskDto();

		dto.setBusinessRuleTaskId(entity.getBusinessRuleTaskId());
		dto.setXmlId(entity.getBusinessRuleTaskId().toString() + "_" + entity.getXmlId());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setRuleId(entity.getRuleId());

		return dto;
	}

	public static List<BusinessRuleTask> toEntityList(final List<BusinessRuleTaskDto> dtoList, Process process) {
		List<BusinessRuleTask> entityList = new ArrayList<BusinessRuleTask>(0);
		if (dtoList != null) {
			for (BusinessRuleTaskDto dto : dtoList) {
				BusinessRuleTask entity = dto.toEntity(process);
				entityList.add(entity);
			}
		}
		return entityList;
	}

	public static List<BusinessRuleTaskDto> toDtoList(final List<BusinessRuleTask> entityList) {
		List<BusinessRuleTaskDto> dtoList = new ArrayList<BusinessRuleTaskDto>(0);
		if (entityList != null) {
			for (BusinessRuleTask entity : entityList) {
				BusinessRuleTaskDto dto = BusinessRuleTaskDto.toDto(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
}
