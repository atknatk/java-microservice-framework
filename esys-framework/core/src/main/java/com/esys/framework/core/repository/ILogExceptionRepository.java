package com.esys.framework.core.repository;

import com.esys.framework.core.entity.core.LogEvent;
import com.esys.framework.core.entity.core.LogException;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILogExceptionRepository extends DataTablesRepository<LogException,Long> {

}
