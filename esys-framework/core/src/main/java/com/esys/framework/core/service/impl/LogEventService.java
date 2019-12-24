package com.esys.framework.core.service.impl;

import com.codahale.metrics.annotation.Timed;
import com.esys.framework.core.aop.logging.NoLogging;
import com.esys.framework.core.dto.base.LogEventDto;
import com.esys.framework.core.entity.core.LogEvent;
import com.esys.framework.core.repository.ILogEventRepository;
import com.esys.framework.core.security.SecurityUtils;
import com.esys.framework.core.service.ILogEventService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
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
public class LogEventService extends BaseService<LogEventDto, LogEvent> implements ILogEventService {


    private ILogEventRepository repository;

    @Autowired
    public LogEventService(ModelMapper mapper,ILogEventRepository repository) {
        super(mapper, log, LogEventDto.class, LogEvent.class,repository);
        this.repository = repository;
    }

    @Override
    @NoLogging
    public void saveLog(LogEvent event) {
        repository.save(event);
    }

    @Override
    @Timed
    @NoLogging
    @PreAuthorize("hasAnyAuthority('authority.log.view','authority.log.ownview')")
    public DataTablesOutput<LogEventDto> eventLogPaging(@Valid DataTablesInput input) {
        Function<LogEvent, LogEventDto> toDto = entity -> toDto(entity);

        if(SecurityUtils.getCurrentUserAuthorities().contains("authority.log.view")){
          return repository.findAll(input,toDto);
        }
        input.getColumn("createdBy").getSearch().setValue(SecurityUtils.getCurrentUserLogin());
        return repository.findAll(input,toDto);
    }

    @Override
    protected boolean exist(LogEventDto dto) {
        return false;
    }
}
