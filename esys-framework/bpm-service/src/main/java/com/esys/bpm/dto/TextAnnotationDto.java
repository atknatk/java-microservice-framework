package com.esys.bpm.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.esys.bpm.entity.Process;
import com.esys.bpm.entity.TextAnnotation;
import com.esys.bpm.enums.ProcessComponent;
import com.esys.bpm.utils.CommonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)

@XmlRootElement(name = "textAnnotation")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class TextAnnotationDto {

	@XmlTransient
	private ProcessComponent component = ProcessComponent.TEXT_ANNOTATION;

	@XmlAttribute(name = "id")
	private String xmlId;
	@XmlTransient
	private Long textAnnotationId;
	@XmlElement
	private String text;

	public TextAnnotation toEntity(Process process) {

		TextAnnotation entity = new TextAnnotation();

		entity.setTextAnnotationId(CommonUtil.parseIdFromXmlId(this.getXmlId()));
		entity.setXmlId(CommonUtil.parseOriginalXmlIdFromXmlId(this.getXmlId()));

		entity.setText(this.getText());

		if (process != null)
			entity.setProcess(process);

		return entity;
	}

	public static TextAnnotationDto toDto(final TextAnnotation entity) {

		TextAnnotationDto dto = new TextAnnotationDto();
		dto.setTextAnnotationId(entity.getTextAnnotationId());
		dto.setXmlId(entity.getTextAnnotationId().toString() + "_" + entity.getXmlId());
		dto.setText(entity.getText());

		return dto;
	}

	public static List<TextAnnotation> toEntityList(final List<TextAnnotationDto> dtoList, Process process) {
		List<TextAnnotation> entityList = new ArrayList<TextAnnotation>(0);
		if (dtoList != null) {
			for (TextAnnotationDto dto : dtoList) {
				TextAnnotation entity = dto.toEntity(process);
				entityList.add(entity);
			}
		}
		return entityList;
	}

	public static List<TextAnnotationDto> toDtoList(final List<TextAnnotation> entityList) {
		List<TextAnnotationDto> dtoList = new ArrayList<TextAnnotationDto>(0);
		if (entityList != null) {
			for (TextAnnotation entity : entityList) {
				TextAnnotationDto dto = TextAnnotationDto.toDto(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
}
