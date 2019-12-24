package com.esys.bpm.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.esys.bpm.entity.Process;
import com.esys.bpm.entity.SequenceFlow;
import com.esys.bpm.enums.ProcessComponent;
import com.esys.bpm.utils.CommonUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;

@XmlRootElement(name = "sequenceFlow")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@EqualsAndHashCode(callSuper = false)
public class SequenceFlowDto extends BpmBaseDto {

	@XmlTransient
	private ProcessComponent component = ProcessComponent.SEQUENCE_FLOW;

	@XmlTransient
	private Long sequenceFlowId;

	@XmlAttribute(name = "sourceRef")
	private String sourceRefXmlId;
	@XmlAttribute(name = "targetRef")
	private String targetRefXmlId;

	public SequenceFlow toEntity(Process process) {

		SequenceFlow entity = new SequenceFlow();

		entity.setSequenceFlowId(CommonUtil.parseIdFromXmlId(this.getXmlId()));
		entity.setXmlId(CommonUtil.parseOriginalXmlIdFromXmlId(this.getXmlId()));

		entity.setSourceRefId(CommonUtil.parseIdFromXmlId(this.getSourceRefXmlId()));
		entity.setSourceRefXmlId(this.getSourceRefXmlId());
		entity.setTargetRefId(CommonUtil.parseIdFromXmlId(this.getTargetRefXmlId()));
		entity.setTargetRefXmlId(this.getTargetRefXmlId());

		if (process != null)
			entity.setProcess(process);

		return entity;
	}

	public static SequenceFlowDto toDto(final SequenceFlow entity) {

		SequenceFlowDto dto = new SequenceFlowDto();
		dto.setSequenceFlowId(entity.getSequenceFlowId());
		dto.setXmlId(entity.getSequenceFlowId().toString() + "_" + entity.getXmlId());

		dto.setSourceRefXmlId(entity.getSourceRefId() + "_" + entity.getSourceRefXmlId());
		dto.setTargetRefXmlId(entity.getTargetRefId() + "_" + entity.getTargetRefXmlId());

		return dto;
	}

	public static List<SequenceFlow> toEntityList(final List<SequenceFlowDto> dtoList, Process process) {
		List<SequenceFlow> entityList = new ArrayList<SequenceFlow>(0);
		if (dtoList != null) {
			for (SequenceFlowDto dto : dtoList) {
				SequenceFlow entity = dto.toEntity(process);
				entityList.add(entity);
			}
		}
		return entityList;
	}

	public static List<SequenceFlowDto> toDtoList(final List<SequenceFlow> entityList) {
		List<SequenceFlowDto> dtoList = new ArrayList<SequenceFlowDto>(0);
		if (entityList != null) {
			for (SequenceFlow entity : entityList) {
				SequenceFlowDto dto = SequenceFlowDto.toDto(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
}
