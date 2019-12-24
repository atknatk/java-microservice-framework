package com.esys.framework.base.repository;

import com.esys.framework.base.dto.ReportVersionDto;
import com.esys.framework.base.entity.ReportVersion;
import com.esys.framework.base.enums.ReportName;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */

@Repository
@Transactional
public interface IReportVersionRepository extends CrudRepository<ReportVersion,Long> {

    //@Query("select rv.reportName,rv.version,rv.sign from ReportVersion  rv where rv.reportName = ?1")
    Optional<ReportVersion> findFirstByReportNameOrderByVersionDesc(ReportName reportName);

    @Query("select rv.version from ReportVersion  rv where rv.reportName = ?1 order by rv.version desc")
    List<Integer> getVersionNoList(ReportName reportName);

    Optional<ReportVersion> findByReportNameAndVersion(ReportName reportName, int version);

    @Query("select new com.esys.framework.base.dto.ReportVersionDto(rv.id,rv.version,rv.reportName,rv.createdBy,rv.createdDate,rv.current) from ReportVersion rv where rv.reportName = ?1 order by rv.version")
    List<ReportVersionDto> version(ReportName reportName);

    @Query("update ReportVersion rv set rv.current = false where rv.reportName = ?1")
    @org.springframework.transaction.annotation.Transactional
    @Modifying
    void updateCurrent(ReportName reportName);
}
