package com.esys.bpm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.esys.bpm.entity.Draft;

public interface DraftRepository extends JpaRepository<Draft, Long>, JpaSpecificationExecutor<Draft> {

}
