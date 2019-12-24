package com.esys.bpm.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.esys.bpm.dto.diagram.DiagramDto;
import com.esys.bpm.entity.Gateway;
import com.esys.bpm.entity.Process;
import com.esys.bpm.entity.ProcessId;
import com.esys.bpm.enums.GatewayType;
import com.esys.bpm.enums.ProcessStatusType;
import com.esys.bpm.utils.CollectionUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@XmlRootElement(name = "process")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class ProcessDto extends BpmBaseDto {

	@XmlTransient
	private Long processId;
	@XmlTransient
	private int version;
	@XmlTransient
	private Long createUserId;
	@XmlTransient
	private LocalDateTime createDate;
	@XmlTransient
	private Long updateUserId;
	@XmlTransient
	private LocalDateTime updateDate;
	@XmlTransient
	private Long companyId;

	/////////////////////////////////////////////////// ProcessInfo///////////////////////////////////////////////
	@XmlTransient
	private ProcessStatusType status;

	@XmlTransient
	private List<ParticipantDto> admins;

	@XmlTransient
	private List<ProcessLogDto> Logs;

	/////////////////////////////////////////////////// Drag&DropUI///////////////////////////////////////////////
	@XmlTransient
	private DiagramDto diagram;

	@XmlElement(name = "startEvent")
	private List<StartEventDto> startEvents;
	@XmlElement(name = "endEvent")
	private List<EndEventDto> endEvents;

	@XmlElement(name = "sequenceFlow")
	private List<SequenceFlowDto> sequenceFlows;
	@XmlElement(name = "association")
	private List<AssociationDto> associations;

	@XmlElement(name = "exclusiveGateway")
	private List<GatewayDto> exclusiveGateways;
	@XmlElement(name = "inclusiveGateway")
	private List<GatewayDto> inclusiveGateways;
	@XmlElement(name = "parallelGateway")
	private List<GatewayDto> parallelGateways;

	@XmlElement(name = "serviceTask")
	private List<ServiceTaskDto> serviceTasks;
	@XmlElement(name = "scriptTask")
	private List<ScriptTaskDto> scriptTasks;
	@XmlElement(name = "businessRuleTask")
	private List<BusinessRuleTaskDto> businessRuleTasks;
	@XmlElement(name = "sendTask")
	private List<NotificationTaskDto> notificationTasks;
	@XmlElement(name = "timerTask")
	private List<TimerTaskDto> timerTasks;
	@XmlElement(name = "userTask")
	private List<UserTaskDto> userTasks;
	@XmlElement(name = "sqlTask")
	private List<SqlTaskDto> sqlTasks;

	@XmlTransient
	@JsonIgnore
	public List<GatewayDto> getAllGateways() {
		List<GatewayDto> gateways = new ArrayList<GatewayDto>();

		if (exclusiveGateways != null)
			gateways.addAll(exclusiveGateways);
		if (inclusiveGateways != null)
			gateways.addAll(inclusiveGateways);
		if (parallelGateways != null)
			gateways.addAll(parallelGateways);

		return gateways;
	}

	@XmlElement(name = "textAnnotation")
	private List<TextAnnotationDto> textAnnotations;

	@XmlElement(name = "subprocess")
	private List<SubprocessDto> subprocesses;

	public Process toEntity() {

		Process entity = new Process();

		entity.setProcessId(new ProcessId(this.getProcessId(), this.getVersion()));
		entity.setXmlId(this.getXmlId());
		entity.setName(this.getName());
		entity.setDescription(this.getDescription());

		entity.setCreateUserId(this.getCreateUserId());
		entity.setCreateDate(this.getCreateDate());
		entity.setUpdateUserId(this.getUpdateUserId());
		entity.setUpdateDate(this.getUpdateDate());
		entity.setCompanyId(this.getCompanyId());

		entity.setStatus(this.getStatus());
		// entity.setAdmins(ParticipantDto.toEntityList(this.getAdmins(), entity,
		// null));
		entity.setLogs(ProcessLogDto.toEntityList(this.getLogs()));

		entity.setStartTasks(StartEventDto.toEntityList(this.getStartEvents(), entity));
		entity.setEndTasks(EndEventDto.toEntityList(this.getEndEvents(), entity));

		entity.setSequenceFlows(SequenceFlowDto.toEntityList(this.getSequenceFlows(), entity));
		entity.setAssociations(AssociationDto.toEntityList(this.getAssociations(), entity));

		List<Gateway> gateways = new ArrayList<Gateway>();
		gateways.addAll(GatewayDto.toEntityList(this.getExclusiveGateways(), entity));
		gateways.addAll(GatewayDto.toEntityList(this.getInclusiveGateways(), entity));
		gateways.addAll(GatewayDto.toEntityList(this.getParallelGateways(), entity));
		entity.setGateways(gateways);

		entity.setServiceTasks(ServiceTaskDto.toEntityList(this.getServiceTasks(), entity));
		entity.setScriptTasks(ScriptTaskDto.toEntityList(this.getScriptTasks(), entity));
		entity.setBusinessRuleTasks(BusinessRuleTaskDto.toEntityList(this.getBusinessRuleTasks(), entity));
		entity.setNotificationTasks(NotificationTaskDto.toEntityList(this.getNotificationTasks(), entity));
		entity.setTimerTasks(TimerTaskDto.toEntityList(this.getTimerTasks(), entity));
		entity.setUserTasks(UserTaskDto.toEntityList(this.getUserTasks(), entity));
		entity.setSqlTasks(SqlTaskDto.toEntityList(this.getSqlTasks(), entity));

		entity.setTextAnnotations(TextAnnotationDto.toEntityList(this.getTextAnnotations(), entity));

		// entity.setSubProcesses(SubprocessDto.toEntityList(this.getSubprocesses()));

		return entity;
	}

	public static ProcessDto toDto(final Process entity) {

		ProcessDto dto = new ProcessDto();
		dto.setProcessId(entity.getProcessId().getId());
		dto.setXmlId(entity.getXmlId());
		dto.setVersion(entity.getProcessId().getVersion());

		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());

		dto.setCreateUserId(entity.getCreateUserId());
		dto.setCreateDate(entity.getCreateDate());
		dto.setUpdateUserId(entity.getUpdateUserId());
		dto.setUpdateDate(entity.getUpdateDate());
		dto.setCompanyId(entity.getCompanyId());

		dto.setStatus(entity.getStatus());
		dto.setAdmins(ParticipantDto.toDtoList(entity.getAdmins()));
		dto.setLogs(ProcessLogDto.toDtoList(entity.getLogs()));

		dto.setStartEvents(StartEventDto.toDtoList(entity.getStartTasks()));
		dto.setEndEvents(EndEventDto.toDtoList(entity.getEndTasks()));

		dto.setAssociations(AssociationDto.toDtoList(entity.getAssociations()));
		dto.setSequenceFlows(SequenceFlowDto.toDtoList(entity.getSequenceFlows()));

		dto.setExclusiveGateways(new ArrayList<GatewayDto>());
		dto.setInclusiveGateways(new ArrayList<GatewayDto>());
		dto.setParallelGateways(new ArrayList<GatewayDto>());
		for (Gateway gateway : entity.getGateways()) {
			if (gateway.getType() == GatewayType.EXCLUSIVE)
				dto.getExclusiveGateways().add(GatewayDto.toDto(gateway));
			if (gateway.getType() == GatewayType.INCLUSIVE)
				dto.getInclusiveGateways().add(GatewayDto.toDto(gateway));
			if (gateway.getType() == GatewayType.PARALLEL)
				dto.getParallelGateways().add(GatewayDto.toDto(gateway));
		}

		dto.setServiceTasks(ServiceTaskDto.toDtoList(entity.getServiceTasks()));
		dto.setScriptTasks(ScriptTaskDto.toDtoList(entity.getScriptTasks()));
		dto.setBusinessRuleTasks(BusinessRuleTaskDto.toDtoList(entity.getBusinessRuleTasks()));
		dto.setNotificationTasks(NotificationTaskDto.toDtoList(entity.getNotificationTasks()));
		dto.setTimerTasks(TimerTaskDto.toDtoList(entity.getTimerTasks()));
		dto.setUserTasks(UserTaskDto.toDtoList(entity.getUserTasks()));
		dto.setSqlTasks(SqlTaskDto.toDtoList(entity.getSqlTasks()));

		dto.setTextAnnotations(TextAnnotationDto.toDtoList(entity.getTextAnnotations()));

		// TODO subprocess

		dto = setIncomingsOutgoings(dto);

		return dto;
	}

	private static ProcessDto setIncomingsOutgoings(ProcessDto processDto) {
		for (SequenceFlowDto sequenceFlowDto : processDto.getSequenceFlows()) {
			for (StartEventDto task : CollectionUtil.safeList(processDto.getStartEvents())) {
				if (task.getXmlId().equals(sequenceFlowDto.getSourceRefXmlId()))
					task.getOutgoings().add(sequenceFlowDto.getXmlId());
				else if (task.getXmlId().equals(sequenceFlowDto.getTargetRefXmlId()))
					task.getIncomings().add(sequenceFlowDto.getXmlId());
			}

			for (EndEventDto task : CollectionUtil.safeList(processDto.getEndEvents())) {
				if (task.getXmlId().equals(sequenceFlowDto.getSourceRefXmlId()))
					task.getOutgoings().add(sequenceFlowDto.getXmlId());
				else if (task.getXmlId().equals(sequenceFlowDto.getTargetRefXmlId()))
					task.getIncomings().add(sequenceFlowDto.getXmlId());
			}

			for (GatewayDto task : CollectionUtil.safeList(processDto.getExclusiveGateways())) {
				if (task.getXmlId().equals(sequenceFlowDto.getSourceRefXmlId()))
					task.getOutgoings().add(sequenceFlowDto.getXmlId());
				else if (task.getXmlId().equals(sequenceFlowDto.getTargetRefXmlId()))
					task.getIncomings().add(sequenceFlowDto.getXmlId());
			}
			for (GatewayDto task : CollectionUtil.safeList(processDto.getInclusiveGateways())) {
				if (task.getXmlId().equals(sequenceFlowDto.getSourceRefXmlId()))
					task.getOutgoings().add(sequenceFlowDto.getXmlId());
				else if (task.getXmlId().equals(sequenceFlowDto.getTargetRefXmlId()))
					task.getIncomings().add(sequenceFlowDto.getXmlId());
			}
			for (GatewayDto task : CollectionUtil.safeList(processDto.getParallelGateways())) {
				if (task.getXmlId().equals(sequenceFlowDto.getSourceRefXmlId()))
					task.getOutgoings().add(sequenceFlowDto.getXmlId());
				else if (task.getXmlId().equals(sequenceFlowDto.getTargetRefXmlId()))
					task.getIncomings().add(sequenceFlowDto.getXmlId());
			}

			for (ServiceTaskDto task : CollectionUtil.safeList(processDto.getServiceTasks())) {
				if (task.getXmlId().equals(sequenceFlowDto.getSourceRefXmlId()))
					task.getOutgoings().add(sequenceFlowDto.getXmlId());
				else if (task.getXmlId().equals(sequenceFlowDto.getTargetRefXmlId()))
					task.getIncomings().add(sequenceFlowDto.getXmlId());
			}

			for (ScriptTaskDto task : CollectionUtil.safeList(processDto.getScriptTasks())) {
				if (task.getXmlId().equals(sequenceFlowDto.getSourceRefXmlId()))
					task.getOutgoings().add(sequenceFlowDto.getXmlId());
				else if (task.getXmlId().equals(sequenceFlowDto.getTargetRefXmlId()))
					task.getIncomings().add(sequenceFlowDto.getXmlId());
			}

			for (BusinessRuleTaskDto task : CollectionUtil.safeList(processDto.getBusinessRuleTasks())) {
				if (task.getXmlId().equals(sequenceFlowDto.getSourceRefXmlId()))
					task.getOutgoings().add(sequenceFlowDto.getXmlId());
				else if (task.getXmlId().equals(sequenceFlowDto.getTargetRefXmlId()))
					task.getIncomings().add(sequenceFlowDto.getXmlId());
			}

			for (NotificationTaskDto task : CollectionUtil.safeList(processDto.getNotificationTasks())) {
				if (task.getXmlId().equals(sequenceFlowDto.getSourceRefXmlId()))
					task.getOutgoings().add(sequenceFlowDto.getXmlId());
				else if (task.getXmlId().equals(sequenceFlowDto.getTargetRefXmlId()))
					task.getIncomings().add(sequenceFlowDto.getXmlId());
			}

			for (TimerTaskDto task : CollectionUtil.safeList(processDto.getTimerTasks())) {
				if (task.getXmlId().equals(sequenceFlowDto.getSourceRefXmlId()))
					task.getOutgoings().add(sequenceFlowDto.getXmlId());
				else if (task.getXmlId().equals(sequenceFlowDto.getTargetRefXmlId()))
					task.getIncomings().add(sequenceFlowDto.getXmlId());
			}

			for (UserTaskDto task : CollectionUtil.safeList(processDto.getUserTasks())) {
				if (task.getXmlId().equals(sequenceFlowDto.getSourceRefXmlId()))
					task.getOutgoings().add(sequenceFlowDto.getXmlId());
				else if (task.getXmlId().equals(sequenceFlowDto.getTargetRefXmlId()))
					task.getIncomings().add(sequenceFlowDto.getXmlId());
			}

			for (SqlTaskDto task : CollectionUtil.safeList(processDto.getSqlTasks())) {
				if (task.getXmlId().equals(sequenceFlowDto.getSourceRefXmlId()))
					task.getOutgoings().add(sequenceFlowDto.getXmlId());
				else if (task.getXmlId().equals(sequenceFlowDto.getTargetRefXmlId()))
					task.getIncomings().add(sequenceFlowDto.getXmlId());
			}
		}
		return processDto;
	}
}
