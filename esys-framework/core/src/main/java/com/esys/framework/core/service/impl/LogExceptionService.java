package com.esys.framework.core.service.impl;

import com.codahale.metrics.annotation.Timed;
import com.esys.framework.core.aop.logging.NoLogging;
import com.esys.framework.core.dto.base.LogEventDto;
import com.esys.framework.core.dto.base.LogExceptionDto;
import com.esys.framework.core.entity.core.LogEvent;
import com.esys.framework.core.entity.core.LogException;
import com.esys.framework.core.repository.ILogEventRepository;
import com.esys.framework.core.repository.ILogExceptionRepository;
import com.esys.framework.core.security.SecurityUtils;
import com.esys.framework.core.service.ILogEventService;
import com.esys.framework.core.service.ILogExceptionService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.function.Function;

@Transactional
@Service
@Slf4j
public class LogExceptionService extends BaseService<LogExceptionDto, LogException> implements ILogExceptionService {


    private ILogExceptionRepository repository;

    @Autowired
    public LogExceptionService(ModelMapper mapper, ILogExceptionRepository repository) {
        super(mapper, log, LogExceptionDto.class, LogException.class,repository);
        this.repository = repository;
    }

    @Override
    @NoLogging
    public void saveLog(LogException event) {
        repository.save(event);
    }

    @Override
    @Timed
    @NoLogging
    @PreAuthorize("hasAnyAuthority('authority.log.view')")
    public DataTablesOutput<LogExceptionDto> paging(@Valid DataTablesInput input) {
        return repository.findAll(input,entity -> toDto(entity));
    }

    @Override
    protected boolean exist(LogExceptionDto dto) {
        return false;
    }
}
