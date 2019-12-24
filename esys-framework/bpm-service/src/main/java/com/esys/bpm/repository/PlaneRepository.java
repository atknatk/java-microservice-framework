package com.esys.bpm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.esys.bpm.entity.Process;
import com.esys.bpm.entity.diagram.Plane;

public interface PlaneRepository extends JpaRepository<Plane, Long>, JpaSpecificationExecutor<Plane> {

	Plane findByProcess(Process process);
}
