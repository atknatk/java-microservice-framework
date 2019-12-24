package com.esys.bpm.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esys.bpm.common.BpmBaseResult;
import com.esys.bpm.dto.DraftComponentDto;
import com.esys.bpm.dto.DraftDto;
import com.esys.bpm.entity.Draft;
import com.esys.bpm.entity.DraftComponent;
import com.esys.bpm.enums.ProcessStatusType;
import com.esys.bpm.repository.DraftComponentRepository;
import com.esys.bpm.repository.DraftRepository;
import com.esys.bpm.service.IDraftService;

@Service("draftService")
public class DraftServiceImpl implements IDraftService {

	@Autowired
	private DraftRepository draftRepository;

	@Autowired
	private DraftComponentRepository draftComponentRepository;

	@Override
	public BpmBaseResult<Long> saveDraft(Long id, String name, String xml) {
		BpmBaseResult<Long> result = new BpmBaseResult<Long>();

		DraftDto dto = new DraftDto();
		if (id != -1) {
			dto.setDraftId(id);
		}
		dto.setName(name);
		dto.setVersion(1L);
		dto.setStatus(ProcessStatusType.DRAFT.toString());
		dto.setResponsibleUser("ESYS");
		dto.setCreateUser("ESYS");
		dto.setCreateDate(LocalDateTime.now());
		dto.setLastModifiedUser("ESYS");
		dto.setLastModifiedDate(LocalDateTime.now());
		dto.setXml(xml);

		try {
			Draft entity = dto.toEntity();
			entity = draftRepository.save(entity);
			result.setData(entity.getDraftId());
		} catch (Exception ex) {
			result.addErrorMessage(ex.getMessage());
		}

		return result;
	}

	@Override
	public BpmBaseResult<Long> saveDraftComponent(Long draftId, String xmlId, String xml) {
		BpmBaseResult<Long> result = new BpmBaseResult<Long>();
		DraftComponent newEntity;

		Optional<DraftComponent> entity = draftComponentRepository.findByXmlIdAndDraftId(xmlId, draftId);
		if (!entity.isPresent()) {
			newEntity = new DraftComponent();
			newEntity.setDraftId(draftId);
			newEntity.setXmlId(xmlId);
			newEntity.setXml(xml);
		} else {
			newEntity = entity.get();
			newEntity.setXml(xml);
		}

		newEntity = draftComponentRepository.save(newEntity);
		result.setData(newEntity.getDraftComponentId());

		return result;
	}

	@Override
	public BpmBaseResult<List<DraftDto>> getDrafts() {
		BpmBaseResult<List<DraftDto>> result = new BpmBaseResult<List<DraftDto>>();

		List<DraftDto> dtoList = new ArrayList<DraftDto>();
		List<Draft> entityList = draftRepository.findAll();

		for (Draft entity : entityList) {
			List<DraftComponentDto> components = DraftComponentDto
					.toDtoList(draftComponentRepository.findAllByDraftId(entity.getDraftId()));
			dtoList.add(DraftDto.toDto(entity, components));
		}

		result.setData(dtoList);

		return result;
	}

}
