package com.esys.bpm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.esys.bpm.entity.Process;
import com.esys.bpm.entity.ProcessId;

public interface ProcessRepository extends JpaRepository<Process, ProcessId>, JpaSpecificationExecutor<Process> {

	List<Process> findByCompanyId(Long companyId);

	Process getByProcessId(ProcessId processId);

	@Query(value = "select coalesce(max(p.id), 0) from Process p ", nativeQuery = true)
	Long findMaxId();

	@Query(value = "select max(p.version) from Process p where p.id = :id", nativeQuery = true)
	Long findMaxVersion(Long id);

	/*
	 * @Query("SELECT r FROM Role r WHERE r.isDefault = :default") Set<Role>
	 * findRolesByDefault(@Param("default") boolean _default);
	 */
}
