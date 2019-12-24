 package com.esys.main.controller.input.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.esys.main.dto.ActionHeadersDTO;
import com.esys.main.entity.RengineAction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateActionInput {
	 		

		private Long actionId;
		private String actionName;
		private String ruleName;
		private String pageName;
		private Long pageRuleId;
		private Long pageId;
		private String actionType;
		private String actionBody;	
		private String httpMethod;
		private String url;
		private String contentType;
		private List<ActionHeadersDTO> headers;
		private Date createDate;
		private String platform;

		public RengineAction toEntity() {
			
			RengineAction entity = new RengineAction();
			entity.setActionBody(this.getActionBody());
			entity.setActionId(this.getActionId());
			entity.setActionName(this.getActionName());
			entity.setActionType(this.getActionType());
			entity.setContentType(this.getContentType());
			entity.setCreateDate(this.getCreateDate());
			if(this.getHeaders()!=null) {
				entity.setHeaders(ActionHeadersDTO.toEntityList(this.getHeaders()));
			}
			entity.setHttpMethod(this.getHttpMethod());
			entity.setPlatform(this.getPlatform());
			entity.setPageRuleId(this.getPageRuleId());
			entity.setUrl(this.getUrl());
			entity.setPageId(this.getPageId());
			return entity;
		}
		
		public static RengineAction toEntity(UpdateActionInput input) {
			RengineAction entity = new RengineAction();
			entity.setActionBody(input.getActionBody());
			entity.setActionId(input.getActionId());
			entity.setActionName(input.getActionName());
			entity.setActionType(input.getActionType());
			entity.setContentType(input.getContentType());
			entity.setCreateDate(input.getCreateDate());
			if(input.getHeaders()!=null) {
				entity.setHeaders(ActionHeadersDTO.toEntityList(input.getHeaders()));
			}
			entity.setHttpMethod(input.getHttpMethod());
			entity.setPlatform(input.getPlatform());
			entity.setPageRuleId(input.getPageRuleId());
			entity.setUrl(input.getUrl());	
			entity.setPageId(input.getPageId());
			return entity;
		}
		
		public static UpdateActionInput toDTO(final RengineAction entity){
			UpdateActionInput dto=new  UpdateActionInput();
			dto.setActionBody(entity.getActionBody());
			dto.setActionId(entity.getActionId());
			dto.setActionName(entity.getActionName());
			dto.setActionType(entity.getActionType());
			dto.setContentType(entity.getContentType());
			dto.setCreateDate(entity.getCreateDate());
			if(entity.getHeaders()!=null) {
				dto.setHeaders(ActionHeadersDTO.toDTOList(entity.getHeaders()));
			}
			dto.setHttpMethod(entity.getHttpMethod());
			dto.setPlatform(entity.getPlatform());
			dto.setPageRuleId(entity.getPageRuleId());
			dto.setUrl(entity.getUrl());
			dto.setPageId(entity.getPageId());
			return dto;
		}
		
		public static List<UpdateActionInput> toDTOList(final List<RengineAction> entityList){
			List<UpdateActionInput> dtoList=new ArrayList<UpdateActionInput>(0);
			if(entityList!=null) {
				for(RengineAction entity:entityList) {
					UpdateActionInput dto=UpdateActionInput.toDTO(entity);
					dtoList.add(dto);
				}
			}
			return dtoList;
		}
		
		public static List<RengineAction> toEntityList(final List<UpdateActionInput> dtoList){
			List<RengineAction> entityList=new ArrayList<RengineAction>(0);
			if(entityList!=null) {
				for(UpdateActionInput dto:dtoList) {
					RengineAction entity=dto.toEntity();
					entityList.add(entity);
				}
			}
			return entityList;
		}
}
