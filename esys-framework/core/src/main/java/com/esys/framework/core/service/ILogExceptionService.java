package com.esys.framework.core.service;

import com.esys.framework.core.dto.base.LogEventDto;
import com.esys.framework.core.dto.base.LogExceptionDto;
import com.esys.framework.core.entity.core.LogEvent;
import com.esys.framework.core.entity.core.LogException;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface ILogExceptionService {

    void saveLog(LogException event);

    DataTablesOutput<LogExceptionDto> paging(@Valid @RequestBody DataTablesInput input);


}
