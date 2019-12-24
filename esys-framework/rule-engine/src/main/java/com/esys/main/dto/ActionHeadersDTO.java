package com.esys.main.dto;

import java.util.ArrayList;
import java.util.List;

import com.esys.main.entity.RengineActionHeaders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionHeadersDTO {
	
	private Long headerId;
	private String headerName;
	private String headerValue;
	private Long actionId;

	public RengineActionHeaders toEntity() {

		RengineActionHeaders entity = new RengineActionHeaders();
		entity.setHeaderId(this.getHeaderId());
		entity.setActionId(this.getActionId());
		entity.setHeaderName(this.getHeaderName());
		entity.setHeaderValue(this.getHeaderValue());
		return entity;
	}

	public static RengineActionHeaders toEntity(ActionHeadersDTO input) {
		RengineActionHeaders entity = new RengineActionHeaders();
		entity.setHeaderId(input.getHeaderId());
		entity.setActionId(input.getActionId());
		entity.setHeaderName(input.getHeaderName());
		entity.setHeaderValue(input.getHeaderValue());
		return entity;
	}

	public static ActionHeadersDTO toDTO(final RengineActionHeaders entity) {

		ActionHeadersDTO dto = new ActionHeadersDTO();
		dto.setHeaderId(entity.getHeaderId());
		dto.setActionId(entity.getActionId());
		dto.setHeaderName(entity.getHeaderName());
		dto.setHeaderValue(entity.getHeaderValue());
		return dto;
	}

	public static List<ActionHeadersDTO> toDTOList(final List<RengineActionHeaders> entityList) {
		List<ActionHeadersDTO> dtoList = new ArrayList<ActionHeadersDTO>(0);
		if (entityList != null) {
			for (RengineActionHeaders entity : entityList) {
				ActionHeadersDTO dto = ActionHeadersDTO.toDTO(entity);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}

	public static List<RengineActionHeaders> toEntityList(final List<ActionHeadersDTO> dtoList) {
		List<RengineActionHeaders> entityList = new ArrayList<RengineActionHeaders>(0);
		if (entityList != null) {
			for (ActionHeadersDTO dto : dtoList) {
				RengineActionHeaders entity = dto.toEntity();
				entityList.add(entity);
			}
		}
		return entityList;
	}
}
