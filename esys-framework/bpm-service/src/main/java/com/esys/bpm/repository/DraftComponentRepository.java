package com.esys.bpm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.esys.bpm.entity.DraftComponent;

public interface DraftComponentRepository
		extends JpaRepository<DraftComponent, Long>, JpaSpecificationExecutor<DraftComponent> {

	Optional<DraftComponent> findByXmlIdAndDraftId(String xmlId, Long draftId);

	List<DraftComponent> findAllByDraftId(Long draftId);

}
