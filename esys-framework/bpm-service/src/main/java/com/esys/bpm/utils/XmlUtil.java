package com.esys.bpm.utils;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.dto.GatewayDto;
import com.esys.bpm.dto.ProcessDto;
import com.esys.bpm.dto.diagram.DiagramDto;
import com.esys.bpm.enums.GatewayType;

public class XmlUtil {

	public static BpmBaseResult<ProcessDto> convertXmlToDto(String processXml) {
		BpmBaseResult<ProcessDto> result = new BpmBaseResult<ProcessDto>();

		String processLogicXml = "";
		try {
			processLogicXml = processXml
					.substring(processXml.indexOf("<bpmn:process"), processXml.indexOf("<bpmndi:BPMNDiagram"))
					.replace("bpmn:", "");
		} catch (Exception e) {
			e.printStackTrace();
			result.addErrorMessage(e.getMessage());
		}

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ProcessDto.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			StringReader reader = new StringReader(processLogicXml);
			ProcessDto process = (ProcessDto) unmarshaller.unmarshal(reader);

			for (GatewayDto gateway : CollectionUtil.safeList(process.getExclusiveGateways()))
				gateway.setType(GatewayType.EXCLUSIVE);
			for (GatewayDto gateway : CollectionUtil.safeList(process.getInclusiveGateways()))
				gateway.setType(GatewayType.INCLUSIVE);
			for (GatewayDto gateway : CollectionUtil.safeList(process.getParallelGateways()))
				gateway.setType(GatewayType.PARALLEL);

			BpmBaseResult<DiagramDto> diagramResult = convertXmlToDiagramDto(processXml);
			if (diagramResult.isSuccessful())
				process.setDiagram(diagramResult.getData());
			else
				result.addMessages(diagramResult.getMessages());

			result.setData(process);
		} catch (JAXBException e) {
			e.printStackTrace();
			result.addErrorMessage(e.getMessage());
		}

		return result;
	}

	private static BpmBaseResult<DiagramDto> convertXmlToDiagramDto(String processXml) {
		BpmBaseResult<DiagramDto> result = new BpmBaseResult<DiagramDto>();

		String processDiagramXml = "";
		try {
			processDiagramXml = processXml
					.substring(processXml.indexOf("<bpmndi:BPMNDiagram"), processXml.indexOf("</bpmn:definitions>"))
					.replace("bpmndi:BPMN", "").replace("dc:", "").replace("di:", "");
		} catch (Exception e) {
			e.printStackTrace();
			result.addErrorMessage(e.getMessage());
		}

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(DiagramDto.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			StringReader reader = new StringReader(processDiagramXml);
			DiagramDto diagram = (DiagramDto) unmarshaller.unmarshal(reader);

			result.setData(diagram);
		} catch (JAXBException e) {
			e.printStackTrace();
			result.addErrorMessage("Diagram " + e.getMessage());
		}

		return result;
	}

	public static BpmBaseResult<String> convertDtoToXml(ProcessDto process) {
		BpmBaseResult<String> result = new BpmBaseResult<String>();

		BpmBaseResult<String> processResult = convertProcessDtoToXml(process);
		BpmBaseResult<String> diagramResult = new BpmBaseResult<String>();

		if (processResult.isSuccessful()) {
			diagramResult = convertDiagramDtoToXml(process.getDiagram());
		}

		if (processResult.isSuccessful() && diagramResult.isSuccessful()) {
			String xmlString = processResult.getData() + diagramResult.getData();

			xmlString = xmlString.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
			xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<bpmn:definitions xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" id=\"Definitions_0w2nuqk\" targetNamespace=\"http://bpmn.io/schema/bpmn\">"
					+ xmlString;
			xmlString = xmlString + "</bpmn:definitions>";
			result.setData(xmlString);
		}

		result.addMessages(processResult.getMessages());
		result.addMessages(diagramResult.getMessages());

		return result;
	}

	private static BpmBaseResult<String> convertProcessDtoToXml(ProcessDto process) {
		BpmBaseResult<String> result = new BpmBaseResult<String>();

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ProcessDto.class);
			StringWriter sw = new StringWriter();

			Marshaller marshaller = jaxbContext.createMarshaller();
			// marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			// For formatted output.
			marshaller.marshal(process, sw);

			String xmlString = sw.toString();

			xmlString = xmlString.replace("<incoming>", "<bpmn:incoming>");
			xmlString = xmlString.replace("</incoming>", "</bpmn:incoming>");
			xmlString = xmlString.replace("<outgoing>", "<bpmn:outgoing>");
			xmlString = xmlString.replace("</outgoing>", "</bpmn:outgoing>");

			xmlString = xmlString.replace("<association", "<bpmn:association");
			xmlString = xmlString.replace("<businessRuleTask", "<bpmn:businessRuleTask");
			xmlString = xmlString.replace("<endEvent", "<bpmn:endEvent");
			xmlString = xmlString.replace("<exclusiveGateway", "<bpmn:exclusiveGateway");
			xmlString = xmlString.replace("<inclusiveGateway", "<bpmn:inclusiveGateway");
			xmlString = xmlString.replace("<parallelGateway", "<bpmn:parallelGateway");
			xmlString = xmlString.replace("<sendTask", "<bpmn:sendTask");// NotificationTask
			xmlString = xmlString.replace("<process", "<bpmn:process");
			xmlString = xmlString.replace("<scriptTask", "<bpmn:scriptTask");
			xmlString = xmlString.replace("<sequenceFlow", "<bpmn:sequenceFlow");
			xmlString = xmlString.replace("<serviceTask", "<bpmn:serviceTask");
			xmlString = xmlString.replace("<sqlTask", "<bpmn:sqlTask");
			xmlString = xmlString.replace("<startEvent", "<bpmn:startEvent");
			xmlString = xmlString.replace("<subprocess", "<bpmn:subprocess");
			xmlString = xmlString.replace("<textAnnotation", "<bpmn:textAnnotation");
			xmlString = xmlString.replace("<timerTask", "<bpmn:timerTask");
			xmlString = xmlString.replace("<userTask", "<bpmn:userTask");

			xmlString = xmlString.replace("<sequenceFlow", "<bpmn:sequenceFlow");

			xmlString = xmlString.replace("association>", "bpmn:association>");
			xmlString = xmlString.replace("businessRuleTask>", "bpmn:businessRuleTask>");
			xmlString = xmlString.replace("endEvent>", "bpmn:endEvent>");
			xmlString = xmlString.replace("exclusiveGateway>", "bpmn:exclusiveGateway>");
			xmlString = xmlString.replace("inclusiveGateway>", "bpmn:inclusiveGateway>");
			xmlString = xmlString.replace("parallelGateway>", "bpmn:parallelGateway>");
			xmlString = xmlString.replace("sendTask>", "bpmn:sendTask>");// NotificationTask
			xmlString = xmlString.replace("process>", "bpmn:process>");
			xmlString = xmlString.replace("scriptTask>", "bpmn:scriptTask>");
			xmlString = xmlString.replace("sequenceFlow>", "bpmn:sequenceFlow>");
			xmlString = xmlString.replace("serviceTask>", "bpmn:serviceTask>");
			xmlString = xmlString.replace("sqlTask>", "bpmn:sqlTask>");
			xmlString = xmlString.replace("startEvent>", "bpmn:startEvent>");
			xmlString = xmlString.replace("subprocess>", "bpmn:subprocess>");
			xmlString = xmlString.replace("textAnnotation>", "bpmn:textAnnotation>");
			xmlString = xmlString.replace("timerTask>", "bpmn:timerTask>");
			xmlString = xmlString.replace("userTask>", "bpmn:userTask>");
			xmlString = xmlString.replace("sequenceFlow>", "bpmn:sequenceFlow>");

			result.setData(xmlString);

		} catch (JAXBException e) {
			e.printStackTrace();
			result.addErrorMessage(e.getMessage());
		}

		return result;
	}

	private static BpmBaseResult<String> convertDiagramDtoToXml(DiagramDto diagram) {
		BpmBaseResult<String> result = new BpmBaseResult<String>();

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(DiagramDto.class);
			StringWriter sw = new StringWriter();

			Marshaller marshaller = jaxbContext.createMarshaller();
			// marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			// For formatted output.
			marshaller.marshal(diagram, sw);

			String xmlString = sw.toString();

			xmlString = xmlString.replace("<Bounds", "<dc:Bounds");
			xmlString = xmlString.replace("<Diagram", "<bpmndi:BPMNDiagram");
			xmlString = xmlString.replace("<Edge", "<bpmndi:BPMNEdge");
			xmlString = xmlString.replace("<Label", "<bpmndi:BPMNLabel");
			xmlString = xmlString.replace("<Plane", "<bpmndi:BPMNPlane");
			xmlString = xmlString.replace("<Shape", "<bpmndi:BPMNShape");
			xmlString = xmlString.replace("<Waypoint", "<di:waypoint");

			xmlString = xmlString.replace("Bounds>", "dc:Bounds>");
			xmlString = xmlString.replace("Diagram>", "bpmndi:BPMNDiagram>");
			xmlString = xmlString.replace("Edge>", "bpmndi:BPMNEdge>");
			xmlString = xmlString.replace("Label>", "bpmndi:BPMNLabel>");
			xmlString = xmlString.replace("Plane>", "bpmndi:BPMNPlane>");
			xmlString = xmlString.replace("Shape>", "bpmndi:BPMNShape>");
			xmlString = xmlString.replace("Waypoint>", "di:waypoint>");

			result.setData(xmlString);

		} catch (JAXBException e) {
			e.printStackTrace();
			result.addErrorMessage(e.getMessage());
		}

		return result;
	}
}
