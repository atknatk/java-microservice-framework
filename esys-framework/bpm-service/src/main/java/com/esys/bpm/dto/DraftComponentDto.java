package com.esys.bpm.dto;

import java.util.ArrayList;
import java.util.List;

import com.esys.bpm.entity.DraftComponent;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DraftComponentDto {

	private Long draftComponentId;
	private Long draftId;
	private String xmlId;
	private String xml;

	public DraftComponent toEntity() {

		DraftComponent entity = new DraftComponent();
		entity.setDraftComponentId(this.getDraftComponentId());
		entity.setDraftId(this.getDraftId());
		entity.setXmlId(this.getXmlId());
		entity.setXml(this.getXml());

		return entity;
	}

	public static DraftComponentDto toDto(final DraftComponent entity) {
		DraftComponentDto dto = new DraftComponentDto();

		dto.setDraftComponentId(entity.getDraftComponentId());
		dto.setDraftId(entity.getDraftId());
		dto.setXmlId(entity.getXmlId());
		dto.setXml(entity.getXml());

		return dto;
	}

	public static List<DraftComponentDto> toDtoList(final List<DraftComponent> entityList) {
		List<DraftComponentDto> dtoList = new ArrayList<DraftComponentDto>(0);
		if (entityList != null) {
			for (DraftComponent entity : entityList) {
				DraftComponentDto dto = DraftComponentDto.toDto(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}

}
