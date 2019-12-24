package com.esys.framework.core.repository;

import com.esys.framework.core.entity.core.LogEvent;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILogEventRepository extends DataTablesRepository<LogEvent,Long> {
}
