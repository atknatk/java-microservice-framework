package com.esys.bpm.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.esys.bpm.entity.Association;
import com.esys.bpm.entity.Process;
import com.esys.bpm.enums.ProcessComponent;
import com.esys.bpm.utils.CommonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)

@XmlRootElement(name = "association")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class AssociationDto extends BpmBaseDto {

	@XmlTransient
	private ProcessComponent component = ProcessComponent.ASSOCIATION;

	@XmlTransient
	private Long associationId;

	@XmlAttribute
	private String sourceRef;
	@XmlAttribute
	private String targetRef;

	public Association toEntity(Process process) {

		Association entity = new Association();

		entity.setAssociationId(CommonUtil.parseIdFromXmlId(this.getXmlId()));
		entity.setXmlId(CommonUtil.parseOriginalXmlIdFromXmlId(this.getXmlId()));

		entity.setSourceRef(this.getSourceRef());
		entity.setTargetRef(this.getTargetRef());

		if (process != null)
			entity.setProcess(process);

		return entity;
	}

	public static AssociationDto toDto(final Association entity) {

		AssociationDto dto = new AssociationDto();
		dto.setAssociationId(entity.getAssociationId());
		dto.setXmlId(entity.getAssociationId().toString() + "_" + entity.getXmlId());
		dto.setSourceRef(entity.getSourceRef());
		dto.setTargetRef(entity.getTargetRef());

		return dto;
	}

	public static List<Association> toEntityList(final List<AssociationDto> dtoList, Process process) {
		List<Association> entityList = new ArrayList<Association>(0);
		if (dtoList != null) {
			for (AssociationDto dto : dtoList) {
				Association entity = dto.toEntity(process);
				entityList.add(entity);
			}
		}
		return entityList;
	}

	public static List<AssociationDto> toDtoList(final List<Association> entityList) {
		List<AssociationDto> dtoList = new ArrayList<AssociationDto>(0);
		if (entityList != null) {
			for (Association entity : entityList) {
				AssociationDto dto = AssociationDto.toDto(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}

}