package com.esys.framework.core.service;

import com.esys.framework.core.dto.base.LogEventDto;
import com.esys.framework.core.entity.core.LogEvent;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface ILogEventService {

    void saveLog(LogEvent event);

    DataTablesOutput<LogEventDto> eventLogPaging(@Valid @RequestBody DataTablesInput input);
}
