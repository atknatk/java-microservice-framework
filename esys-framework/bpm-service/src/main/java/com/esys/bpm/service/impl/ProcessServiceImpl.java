package com.esys.bpm.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.common.MessageText;
import com.esys.bpm.common.SequenceFlowValidation;
import com.esys.bpm.common.TempClass;
import com.esys.bpm.common.enums.OperationResult;
import com.esys.bpm.dto.ElementFinder;
import com.esys.bpm.dto.EventDto;
import com.esys.bpm.dto.GatewayDto;
import com.esys.bpm.dto.ProcessDto;
import com.esys.bpm.dto.SequenceFlowDto;
import com.esys.bpm.dto.TaskDto;
import com.esys.bpm.dto.diagram.DiagramDto;
import com.esys.bpm.dto.diagram.PlaneDto;
import com.esys.bpm.entity.Association;
import com.esys.bpm.entity.BusinessRuleTask;
import com.esys.bpm.entity.EndTask;
import com.esys.bpm.entity.Gateway;
import com.esys.bpm.entity.NotificationTask;
import com.esys.bpm.entity.Process;
import com.esys.bpm.entity.ProcessId;
import com.esys.bpm.entity.ScriptTask;
import com.esys.bpm.entity.SequenceFlow;
import com.esys.bpm.entity.ServiceTask;
import com.esys.bpm.entity.SqlTask;
import com.esys.bpm.entity.StartTask;
import com.esys.bpm.entity.TextAnnotation;
import com.esys.bpm.entity.TimerTask;
import com.esys.bpm.entity.UserTask;
import com.esys.bpm.entity.diagram.Edge;
import com.esys.bpm.entity.diagram.Plane;
import com.esys.bpm.entity.diagram.Shape;
import com.esys.bpm.enums.ProcessComponent;
import com.esys.bpm.enums.ProcessStatusType;
import com.esys.bpm.repository.PlaneRepository;
import com.esys.bpm.repository.ProcessRepository;
import com.esys.bpm.repository.SequenceFlowRepository;
import com.esys.bpm.service.IBusinessRuleService;
import com.esys.bpm.service.IGatewayService;
import com.esys.bpm.service.INotificationService;
import com.esys.bpm.service.IProcessService;
import com.esys.bpm.service.IScriptService;
import com.esys.bpm.service.IServiceService;
import com.esys.bpm.service.ISqlService;
import com.esys.bpm.service.ITimerService;
import com.esys.bpm.service.IUserService;
import com.esys.bpm.utils.CollectionUtil;
import com.esys.bpm.utils.CommonUtil;
import com.esys.bpm.utils.XmlUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service("processService")
public class ProcessServiceImpl implements IProcessService {

	@Autowired
	private ProcessRepository processRepository;

	@Autowired
	private PlaneRepository planeRepository;

	@Autowired
	private SequenceFlowRepository sequenceFlowRepository;

	@Autowired
	private IUserService userService;

	@Autowired
	private IServiceService serviceService;

	@Autowired
	private ISqlService sqlService;

	@Autowired
	private IScriptService scriptService;

	@Autowired
	private IBusinessRuleService businessRuleService;

	@Autowired
	private INotificationService notificationService;

	@Autowired
	private ITimerService timerService;

	@Autowired
	private IGatewayService gatewayService;

	@Override
	public BpmBaseResult<List<String>> compileProcess(ProcessDto process) {
		return compile(process);
	}

	@Override
	public BpmBaseResult<List<String>> compileProcess(String processXml) {

		BpmBaseResult<List<String>> result = new BpmBaseResult<List<String>>();
		BpmBaseResult<ProcessDto> convertResult = XmlUtil.convertXmlToDto(processXml);

		if (convertResult.isSuccessful()) {
			ProcessDto process = convertResult.getData();
			result = compile(process);

			if (convertResult.isSuccessful() && result.isSuccessful())
				result.addInfoMessage(MessageText.COMPILE_SUCCESS);
		}

		result.addMessages(convertResult.getMessages());

		return result;
	}

	private BpmBaseResult<List<String>> compile(ProcessDto process) {
		BpmBaseResult<List<String>> result = new BpmBaseResult<List<String>>();
		List<String> errorIdList = new ArrayList<String>();
		result.setData(errorIdList);

		List<SequenceFlowValidation> sequenceValidations = new ArrayList<SequenceFlowValidation>();

		// TODO Aynı isimli birden fazla task var mı

		// Complete Process Validations
		// Start Task
		if (CollectionUtil.isNullOrEmpty(process.getStartEvents()) || process.getStartEvents().size() > 1) {
			result.addErrorMessage(MessageText.EXACTLY_ONE_START);
		} else {
			for (EventDto start : process.getStartEvents()) {
				// For sequence validation
				sequenceValidations.add(new SequenceFlowValidation(ProcessComponent.START, start.getXmlId()));
			}
		}
		// End Task
		if (CollectionUtil.isNullOrEmpty(process.getEndEvents())) {
			result.addErrorMessage(MessageText.AT_LEAST_ONE_END);
		} else {
			for (EventDto start : process.getEndEvents()) {
				// For sequence validation
				sequenceValidations.add(new SequenceFlowValidation(ProcessComponent.END, start.getXmlId()));
			}
		}

		// Task
		if (CollectionUtil.isNullOrEmpty(process.getServiceTasks())
				&& CollectionUtil.isNullOrEmpty(process.getScriptTasks())
				&& CollectionUtil.isNullOrEmpty(process.getBusinessRuleTasks())
				&& CollectionUtil.isNullOrEmpty(process.getNotificationTasks())
				&& CollectionUtil.isNullOrEmpty(process.getTimerTasks())
				&& CollectionUtil.isNullOrEmpty(process.getUserTasks())
				&& CollectionUtil.isNullOrEmpty(process.getSqlTasks())) {
			result.addErrorMessage(MessageText.AT_LEAST_ONE_TASK);
		}

		// Individual Process Validations
		// Service Task Validation
		for (TaskDto task : CollectionUtil.safeList(process.getServiceTasks())) {
			result = validateIndividualTask(result, task);
			sequenceValidations.add(new SequenceFlowValidation(ProcessComponent.TASK, task.getXmlId()));
		}
		// Script Task Validation
		for (TaskDto task : CollectionUtil.safeList(process.getScriptTasks())) {
			result = validateIndividualTask(result, task);
			sequenceValidations.add(new SequenceFlowValidation(ProcessComponent.TASK, task.getXmlId()));
		}
		// Business Rule Task Validation
		for (TaskDto task : CollectionUtil.safeList(process.getBusinessRuleTasks())) {
			result = validateIndividualTask(result, task);
			sequenceValidations.add(new SequenceFlowValidation(ProcessComponent.TASK, task.getXmlId()));
		}
		// Notification Task Validation
		for (TaskDto task : CollectionUtil.safeList(process.getNotificationTasks())) {
			result = validateIndividualTask(result, task);
			sequenceValidations.add(new SequenceFlowValidation(ProcessComponent.TASK, task.getXmlId()));
		}
		// Timer Task Validation
		for (TaskDto task : CollectionUtil.safeList(process.getTimerTasks())) {
			result = validateIndividualTask(result, task);
			sequenceValidations.add(new SequenceFlowValidation(ProcessComponent.TASK, task.getXmlId()));
		}
		// User Task Validation
		for (TaskDto task : CollectionUtil.safeList(process.getUserTasks())) {
			result = validateIndividualTask(result, task);
			sequenceValidations.add(new SequenceFlowValidation(ProcessComponent.TASK, task.getXmlId()));
		}
		// Sql Task Validation
		for (TaskDto task : CollectionUtil.safeList(process.getSqlTasks())) {
			result = validateIndividualTask(result, task);
			sequenceValidations.add(new SequenceFlowValidation(ProcessComponent.TASK, task.getXmlId()));
		}

		// Gateway Validation
		for (GatewayDto gateway : CollectionUtil.safeList(process.getAllGateways())) {
			// For sequence validation
			sequenceValidations.add(new SequenceFlowValidation(ProcessComponent.GATEWAY, gateway.getXmlId()));

			BpmBaseResult validationResult = gatewayService.validateGateway(gateway);
			result.addMessages(validationResult.getMessages());
			if (validationResult.getData() != null)
				errorIdList.add(gateway.getXmlId());
		}
		// Sequence Flow
		for (SequenceFlowDto sequence : CollectionUtil.safeList(process.getSequenceFlows())) {
			SequenceFlowValidation incoming = sequenceValidations.stream()
					.filter(obj -> sequence.getSourceRefXmlId().equals(obj.getXmlId())).findFirst().orElse(null);
			incoming.increaseSourceRefCount();

			SequenceFlowValidation outgoing = sequenceValidations.stream()
					.filter(obj -> sequence.getTargetRefXmlId().equals(obj.getXmlId())).findFirst().orElse(null);
			outgoing.increaseTargetRefCount();
		}
		for (SequenceFlowValidation sequenceValidation : CollectionUtil.safeList(sequenceValidations)) {
			// Each task, gateway, end must have targeted at least once
			if (!sequenceValidation.getComponent().equals(ProcessComponent.START)
					&& sequenceValidation.getTargetRefCount() == 0) {
				result.addErrorMessage(MessageText.INVALID_SEQUENCE_INCOMING + sequenceValidation.getXmlId());
				errorIdList.add(sequenceValidation.getXmlId());
			}
			// Each task, start must have exactly one source ref
			if (sequenceValidation.getComponent().equals(ProcessComponent.TASK)
					&& sequenceValidation.getSourceRefCount() != 1) {
				result.addErrorMessage(MessageText.INVALID_TASK_SEQUENCE_OUTGOING + sequenceValidation.getXmlId());
				errorIdList.add(sequenceValidation.getXmlId());
			}
			// Each gateway must have at least one source ref
			if (sequenceValidation.getComponent().equals(ProcessComponent.GATEWAY)
					&& sequenceValidation.getSourceRefCount() == 0) {
				result.addErrorMessage(MessageText.INVALID_GATEWAY_SEQUENCE_OUTGOING + sequenceValidation.getXmlId());
				errorIdList.add(sequenceValidation.getXmlId());
			}
			// Each end must have none source ref
			if (sequenceValidation.getComponent().equals(ProcessComponent.END)
					&& sequenceValidation.getSourceRefCount() != 0) {
				result.addErrorMessage(MessageText.INVALID_END_SEQUENCE_OUTGOING + sequenceValidation.getXmlId());
				errorIdList.add(sequenceValidation.getXmlId());
			}
		}

		if (result.isSuccessful())
			result.addInfoMessage(MessageText.COMPILE_SUCCESS);

		return result;
	}

	private BpmBaseResult<List<String>> validateIndividualTask(BpmBaseResult<List<String>> result, TaskDto task) {

		BpmBaseResult validateResult = new BpmBaseResult<>();

		// User Task
		if (task.getComponent().equals(ProcessComponent.USER_TASK)) {
			validateResult = userService.validateTask(task);

		}
		// Service Task
		if (task.getComponent().equals(ProcessComponent.SERVICE_TASK)) {
			validateResult = serviceService.validateTask(task);
		}
		// Sql Task
		if (task.getComponent().equals(ProcessComponent.SQL_TASK)) {
			validateResult = sqlService.validateTask(task);
		}
		// Script Task
		if (task.getComponent().equals(ProcessComponent.SCRIPT_TASK)) {
			validateResult = scriptService.validateTask(task);
		}
		// Business Rule Task
		if (task.getComponent().equals(ProcessComponent.BUSINESS_RULE_TASK)) {
			validateResult = businessRuleService.validateTask(task);
		}
		// Notification Task
		if (task.getComponent().equals(ProcessComponent.NOTIFICATION_TASK)) {
			validateResult = notificationService.validateTask(task);
		}
		// Timer Task
		if (task.getComponent().equals(ProcessComponent.TIMER_TASK)) {
			validateResult = timerService.validateTask(task);
		}

		result.addMessages(validateResult.getMessages());
		if (result.hasErrors())
			result.getData().add(task.getXmlId());

		return result;
	}

	@Override
	public BpmBaseResult<String> saveAsDraft(String processXml) {
		BpmBaseResult<String> result = new BpmBaseResult<String>();
		BpmBaseResult<ProcessDto> convertResult = XmlUtil.convertXmlToDto(processXml);

		if (convertResult.isSuccessful()) {
			result = saveProcess(convertResult.getData(), true);
		}

		result.addMessages(convertResult.getMessages());

		return result;
	}

	@Override
	public BpmBaseResult<String> createProcess(String processXml) {

		BpmBaseResult<String> result = new BpmBaseResult<String>();
		BpmBaseResult<ProcessDto> convertResult = XmlUtil.convertXmlToDto(processXml);
		BpmBaseResult<List<String>> compileResult = new BpmBaseResult<List<String>>();

		if (convertResult.isSuccessful()) {
			compileResult = compileProcess(convertResult.getData());
		}

		if (compileResult.isSuccessful()) {
			result = saveProcess(convertResult.getData(), false);
		}

		result.addMessages(convertResult.getMessages());
		result.addMessages(compileResult.getMessages());

		return result;
	}

	private BpmBaseResult<String> saveProcess(ProcessDto processDto, boolean isDraft) {
		BpmBaseResult<String> result = new BpmBaseResult<String>();

		if (!CommonUtil.isSavedProcess(processDto.getXmlId())) {
			Long maxProcessId = processRepository.findMaxId();
			processDto.setProcessId(maxProcessId + 1);
			processDto.setVersion(1);

			processDto.setName(TempClass.PROCESS_NAME);
			processDto.setDescription(TempClass.PROCESS_DESCRIPTION);
			processDto.setCreateUserId(TempClass.LOGIN_USER);
			processDto.setCreateDate(LocalDateTime.now());
			processDto.setCompanyId(TempClass.COMPANY_ID);
			if (isDraft)
				processDto.setStatus(ProcessStatusType.DRAFT);
			else
				processDto.setStatus(ProcessStatusType.READY);
		} else {
			processDto.setUpdateUserId(TempClass.LOGIN_USER);
			processDto.setUpdateDate(LocalDateTime.now());
		}

		BpmBaseResult<String> processSaveResult = saveProcessTransactional(processDto.toEntity(),
				processDto.getDiagram().getPlane().toEntity());
		if (processSaveResult.isSuccessful()) {
			result.setData(processSaveResult.getData());

			if (isDraft)
				result.addInfoMessage(MessageText.DRAFT_SAVE_SUCCESS + TempClass.PROCESS_NAME);
			else
				result.addInfoMessage(MessageText.PROCESS_SAVE_SUCCESS + TempClass.PROCESS_NAME);
		}

		result.addMessages(processSaveResult.getMessages());

		return result;
	}

	private ElementFinder findIdFromXmlId(Process process, String xmlId) {
		Association association = process.getAssociations().stream().filter(entity -> xmlId.equals(entity.getXmlId()))
				.findAny().orElse(null);
		if (association != null)
			return new ElementFinder(association.getAssociationId(), ProcessComponent.ASSOCIATION);

		BusinessRuleTask businessRuleTask = process.getBusinessRuleTasks().stream()
				.filter(entity -> xmlId.equals(entity.getXmlId())).findAny().orElse(null);
		if (businessRuleTask != null)
			return new ElementFinder(businessRuleTask.getBusinessRuleTaskId(), ProcessComponent.BUSINESS_RULE_TASK);

		EndTask endTask = process.getEndTasks().stream().filter(entity -> xmlId.equals(entity.getXmlId())).findAny()
				.orElse(null);
		if (endTask != null)
			return new ElementFinder(endTask.getEndTaskId(), ProcessComponent.END);

		Gateway gateway = process.getGateways().stream().filter(entity -> xmlId.equals(entity.getXmlId())).findAny()
				.orElse(null);
		if (gateway != null)
			return new ElementFinder(gateway.getGatewayId(), ProcessComponent.GATEWAY);

		NotificationTask notificationTask = process.getNotificationTasks().stream()
				.filter(entity -> xmlId.equals(entity.getXmlId())).findAny().orElse(null);
		if (notificationTask != null)
			return new ElementFinder(notificationTask.getNotificationTaskId(), ProcessComponent.NOTIFICATION_TASK);

		ScriptTask scriptTask = process.getScriptTasks().stream().filter(entity -> xmlId.equals(entity.getXmlId()))
				.findAny().orElse(null);
		if (scriptTask != null)
			return new ElementFinder(scriptTask.getScriptTaskId(), ProcessComponent.SCRIPT_TASK);

		ServiceTask serviceTask = process.getServiceTasks().stream().filter(entity -> xmlId.equals(entity.getXmlId()))
				.findAny().orElse(null);
		if (serviceTask != null)
			return new ElementFinder(serviceTask.getServiceTaskId(), ProcessComponent.SERVICE_TASK);

		SqlTask sqlTask = process.getSqlTasks().stream().filter(entity -> xmlId.equals(entity.getXmlId())).findAny()
				.orElse(null);
		if (sqlTask != null)
			return new ElementFinder(sqlTask.getSqlTaskId(), ProcessComponent.SQL_TASK);

		StartTask startTask = process.getStartTasks().stream().filter(entity -> xmlId.equals(entity.getXmlId()))
				.findAny().orElse(null);
		if (startTask != null)
			return new ElementFinder(startTask.getStartTaskId(), ProcessComponent.START);

		TextAnnotation textAnnotation = process.getTextAnnotations().stream()
				.filter(entity -> xmlId.equals(entity.getXmlId())).findAny().orElse(null);
		if (textAnnotation != null)
			return new ElementFinder(textAnnotation.getTextAnnotationId(), ProcessComponent.TEXT_ANNOTATION);

		TimerTask timerTask = process.getTimerTasks().stream().filter(entity -> xmlId.equals(entity.getXmlId()))
				.findAny().orElse(null);
		if (timerTask != null)
			return new ElementFinder(timerTask.getTimerTaskId(), ProcessComponent.TIMER_TASK);

		UserTask userTask = process.getUserTasks().stream().filter(entity -> xmlId.equals(entity.getXmlId())).findAny()
				.orElse(null);
		if (userTask != null)
			return new ElementFinder(userTask.getUserTaskId(), ProcessComponent.USER_TASK);

		SequenceFlow sequenceFlow = process.getSequenceFlows().stream()
				.filter(entity -> xmlId.equals(entity.getXmlId())).findAny().orElse(null);
		if (sequenceFlow != null)
			return new ElementFinder(sequenceFlow.getSequenceFlowId(), ProcessComponent.SEQUENCE_FLOW);

		return null;
	}

	@Transactional(rollbackOn = Exception.class)
	public BpmBaseResult<String> saveProcessTransactional(Process process, Plane plane) {
		BpmBaseResult<String> result = new BpmBaseResult<String>();

		try {
			process = processRepository.save(process);

			List<SequenceFlow> sequenceFlows = sequenceFlowRepository.findAllByProcess(process);
			for (SequenceFlow sequenceFlow : sequenceFlows) {
				ElementFinder sourceFinder = findIdFromXmlId(process, sequenceFlow.getSourceRefXmlId());
				sequenceFlow.setSourceRefId(sourceFinder.getId());
				sequenceFlow.setSourceRefComponent(sourceFinder.getComponent());

				ElementFinder targetFinder = findIdFromXmlId(process, sequenceFlow.getTargetRefXmlId());
				sequenceFlow.setTargetRefId(targetFinder.getId());
				sequenceFlow.setTargetRefComponent(targetFinder.getComponent());
			}
			sequenceFlowRepository.saveAll(sequenceFlows);

			/////////////////////// Diagram//////////////////////////////
			for (Edge edge : plane.getEdges()) {
				ElementFinder element = findIdFromXmlId(process, edge.getElementXmlId());
				edge.setElementId(element.getId());
				edge.setPlane(plane);
			}
			for (Shape shape : plane.getShapes()) {
				ElementFinder element = findIdFromXmlId(process, shape.getElementXmlId());
				shape.setElementId(element.getId());
				shape.setPlane(plane);
			}

			plane.setProcess(process);
			plane = planeRepository.save(plane);

			ProcessDto processDto = ProcessDto.toDto(process);
			DiagramDto diagramDto = new DiagramDto(PlaneDto.toDto(plane, process.getXmlId()));
			processDto.setDiagram(diagramDto);

			BpmBaseResult<String> convertResult = getProcessXmlByProcess(processDto);
			result.addMessages(convertResult.getMessages());
			if (convertResult.isSuccessful())
				result.setData(convertResult.getData());
		} catch (Exception ex) {
			result.addErrorMessage(ex.getMessage());
			result.setOperationResult(OperationResult.Error);
		}
		return result;
	}

	@Override
	public BpmBaseResult<ProcessDto> getProcessByProcessId(Long processId, int version) {
		BpmBaseResult<ProcessDto> result = new BpmBaseResult<ProcessDto>();

		try {
			Process process = processRepository.getByProcessId(new ProcessId(processId, version));
			ProcessDto processDto = ProcessDto.toDto(process);

			Plane plane = planeRepository.findByProcess(process);
			DiagramDto diagramDto = new DiagramDto(PlaneDto.toDto(plane, process.getXmlId()));
			processDto.setDiagram(diagramDto);

			result.setData(processDto);
		} catch (Exception ex) {
			result.addErrorMessage(ex.getMessage());
		}
		return result;
	}

	@Override
	public BpmBaseResult<String> getProcessXmlByProcessId(Long processId, int version) {
		BpmBaseResult<String> result = new BpmBaseResult<String>();

		BpmBaseResult<ProcessDto> processResult = getProcessByProcessId(processId, version);

		if (processResult.isSuccessful()) {
			result = getProcessXmlByProcess(processResult.getData());
		}

		result.addMessages(processResult.getMessages());

		return result;
	}

	@Override
	public BpmBaseResult<String> getProcessXmlByProcess(ProcessDto process) {
		return XmlUtil.convertDtoToXml(process);
	}

	@Override
	public Boolean isVersionUpgradeNeeded(ProcessDto process) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BpmBaseResult<ProcessDto> getProcessByXml(String processXml) {
		BpmBaseResult<ProcessDto> result = XmlUtil.convertXmlToDto(processXml);

		return result;
	}

}
