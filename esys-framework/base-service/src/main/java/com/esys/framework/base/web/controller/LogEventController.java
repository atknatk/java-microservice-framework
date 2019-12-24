package com.esys.framework.base.web.controller;

import com.esys.framework.core.dto.base.LogEventDto;
import com.esys.framework.core.service.ILogEventService;
import com.esys.framework.core.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */

@RestController
@RequestMapping(value = "/logevent", produces = MediaType.APPLICATION_JSON_VALUE)
public class LogEventController extends BaseController {

    @Autowired
    private ILogEventService eventLogService;


    @RequestMapping(value = "/paging", method = RequestMethod.GET)
    public DataTablesOutput<LogEventDto> getLogs(@Valid DataTablesInput input) {
        return eventLogService.eventLogPaging(input);
    }

}
