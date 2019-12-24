package com.esys.bpm.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.esys.bpm.entity.Draft;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DraftDto {

	private Long draftId;
	private String name;
	private Long version;
	private String status;
	private String responsibleUser;
	private String createUser;
	private LocalDateTime createDate;
	private String lastModifiedUser;
	private LocalDateTime lastModifiedDate;
	private String xml;
	private List<DraftComponentDto> components;

	public Draft toEntity() {

		Draft entity = new Draft();
		entity.setDraftId(this.getDraftId());
		entity.setName(this.getName());
		entity.setVersion(this.getVersion());
		entity.setStatus(this.getStatus());
		entity.setResponsibleUser(this.getResponsibleUser());
		entity.setCreateUser(this.getCreateUser());
		entity.setCreateDate(this.getCreateDate());
		entity.setLastModifiedUser(this.getLastModifiedUser());
		entity.setLastModifiedDate(this.getLastModifiedDate());
		entity.setXml(this.getXml());

		return entity;
	}

	public static DraftDto toDto(final Draft entity, List<DraftComponentDto> components) {
		DraftDto dto = new DraftDto();

		dto.setDraftId(entity.getDraftId());
		dto.setName(entity.getName());
		dto.setVersion(entity.getVersion());
		dto.setStatus(entity.getStatus());
		dto.setResponsibleUser(entity.getResponsibleUser());
		dto.setCreateUser(entity.getCreateUser());
		dto.setCreateDate(entity.getCreateDate());
		dto.setLastModifiedUser(entity.getLastModifiedUser());
		dto.setLastModifiedDate(entity.getLastModifiedDate());
		dto.setXml(entity.getXml());

		dto.setComponents(components);

		/*
		 * List<String> componentStringList = new ArrayList<String>(); for
		 * (DraftComponent component : components) {
		 * componentStringList.add(component.getXml()); }
		 * dto.setComponents(componentStringList);
		 */
		return dto;
	}

}
