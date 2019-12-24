package com.esys.bpm.utils;

import com.esys.bpm.entity.ProcessId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonUtil {

	public static String ConvertObjectToJsonString(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}

	public static boolean isSavedTask(String xmlId) {
		return parseIdFromXmlId(xmlId) != null;
	}

	public static ProcessId parseProcessId(String id) {
		try {
			return new ProcessId(Long.parseLong(id.split("_")[0]), Integer.parseInt(id.split("_")[1]));
		} catch (Exception ex) {
			return null;
		}
	}

	public static Long parseIdFromXmlId(String xmlId) {
		try {
			return Long.parseLong(xmlId.split("_")[0]);
		} catch (Exception ex) {
			return null;
		}
	}

	public static String parseOriginalXmlIdFromXmlId(String xmlId) {
		if (parseLongElseNull(xmlId) == null)
			return xmlId;
		else
			return xmlId.substring(xmlId.indexOf("_") + 1);
	}

	public static Long parseLongElseNull(String id) {
		try {
			return Long.parseLong(id);
		} catch (Exception ex) {
			return null;
		}
	}

	public static boolean isSavedProcess(String id) {
		return parseProcessId(id) != null;
	}

	public static String parseProcessIdToXmlString(ProcessId processId) {
		return processId.toString() + "_" + Integer.toString(processId.getVersion());
	}

}
