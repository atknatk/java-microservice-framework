package com.esys.bpm.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.esys.bpm.common.BpmLookup;
import com.esys.bpm.entity.Process;
import com.esys.bpm.entity.ServiceTask;
import com.esys.bpm.enums.ProcessComponent;
import com.esys.bpm.utils.CollectionUtil;
import com.esys.bpm.utils.CommonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)

@XmlRootElement(name = "serviceTask")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class ServiceTaskDto extends TaskDto {

	@XmlTransient
	private ProcessComponent component = ProcessComponent.SERVICE_TASK;

	@XmlTransient
	private Long serviceTaskId;

	@XmlTransient
	private String serviceUrl;
	/*
	 * @XmlTransient Map<String, String> headers;// = new HashMap<String, String>();
	 */
	@XmlTransient
	List<BpmLookup> headers = new ArrayList<BpmLookup>();

	@XmlTransient
	private String methodName;

	/*
	 * @XmlTransient Map<String, String> parameters;
	 */
	@XmlTransient
	List<BpmLookup> parameters = new ArrayList<BpmLookup>();

	public ServiceTask toEntity() {

		ServiceTask entity = new ServiceTask();

		entity.setServiceTaskId(CommonUtil.parseIdFromXmlId(this.getXmlId()));
		entity.setXmlId(CommonUtil.parseOriginalXmlIdFromXmlId(this.getXmlId()));

		entity.setName(this.getName());
		entity.setDescription(this.getDescription());

		entity.setServiceUrl(this.getServiceUrl());
		/* entity.setHeaders(CollectionUtil.getStringFromMap(this.getHeaders())); */
		entity.setHeaders(CollectionUtil.getStringFromLookupList(this.getHeaders()));
		entity.setMethodName(this.getMethodName());
		/*
		 * entity.setParameters(CollectionUtil.getStringFromMap(this.getParameters()));
		 */
		entity.setParameters(CollectionUtil.getStringFromLookupList(this.getParameters()));

		return entity;
	}

	public ServiceTask toEntity(Process process) {
		ServiceTask entity = toEntity();
		entity.setProcess(process);
		return entity;
	}

	public static ServiceTaskDto toDto(final ServiceTask entity) {
		ServiceTaskDto dto = new ServiceTaskDto();

		dto.setServiceTaskId(entity.getServiceTaskId());
		dto.setXmlId(entity.getServiceTaskId().toString() + "_" + entity.getXmlId());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());

		dto.setServiceUrl(entity.getServiceUrl());
		/* dto.setHeaders(CollectionUtil.getMapFromString(entity.getHeaders())); */
		dto.setHeaders(CollectionUtil.getLookupListFromString(entity.getHeaders()));
		dto.setMethodName(entity.getMethodName());
		/*
		 * dto.setParameters(CollectionUtil.getMapFromString(entity.getParameters()));
		 */
		dto.setParameters(CollectionUtil.getLookupListFromString(entity.getParameters()));

		return dto;
	}

	public static List<ServiceTask> toEntityList(final List<ServiceTaskDto> dtoList, Process process) {
		List<ServiceTask> entityList = new ArrayList<ServiceTask>(0);
		if (dtoList != null) {
			for (ServiceTaskDto dto : dtoList) {
				ServiceTask entity = dto.toEntity(process);
				entityList.add(entity);
			}
		}
		return entityList;
	}

	public static List<ServiceTaskDto> toDtoList(final List<ServiceTask> entityList) {
		List<ServiceTaskDto> dtoList = new ArrayList<ServiceTaskDto>(0);
		if (entityList != null) {
			for (ServiceTask entity : entityList) {
				ServiceTaskDto dto = ServiceTaskDto.toDto(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
}
