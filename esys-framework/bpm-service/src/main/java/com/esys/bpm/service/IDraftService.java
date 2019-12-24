package com.esys.bpm.service;

import java.util.List;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.dto.DraftDto;

public interface IDraftService {

	BpmBaseResult<Long> saveDraft(Long id, String name, String xml);

	BpmBaseResult<Long> saveDraftComponent(Long draftId, String xmlId, String xml);

	BpmBaseResult<List<DraftDto>> getDrafts();

}
