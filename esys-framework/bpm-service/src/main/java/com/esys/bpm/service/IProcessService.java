package com.esys.bpm.service;

import java.util.List;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.dto.ProcessDto;

public interface IProcessService {

	BpmBaseResult<List<String>> compileProcess(String processXml);

	BpmBaseResult<List<String>> compileProcess(ProcessDto process);

	BpmBaseResult<String> saveAsDraft(String processXml);

	BpmBaseResult<String> createProcess(String processXml);

	BpmBaseResult<ProcessDto> getProcessByXml(String processXml);

	BpmBaseResult<ProcessDto> getProcessByProcessId(Long processId, int version);

	BpmBaseResult<String> getProcessXmlByProcess(ProcessDto process);

	BpmBaseResult<String> getProcessXmlByProcessId(Long processId, int version);

	Boolean isVersionUpgradeNeeded(ProcessDto process);

}