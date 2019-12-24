package com.esys.framework.client.contracts.organization;

import com.esys.framework.core.dto.organization.EmployeeDto;
import com.esys.framework.core.model.ModelResult;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.validation.Valid;
import java.util.List;

public interface EmployeeContract {
    @RequestMapping(value = "/employee", produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.GET)
    ModelResult<List<EmployeeDto>> getAllEmployees() throws Exception;


    @RequestMapping(value = "/employee",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ModelResult<EmployeeDto>> saveEmployee(@Valid @RequestBody EmployeeDto employeeDto) throws Exception;

    @RequestMapping(value = "/employee",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ModelResult<EmployeeDto>> updateEmployee(@Valid @RequestBody EmployeeDto employeeDto) throws Exception;

    @RequestMapping(value = "/employee",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ModelResult> deleteEmployee(@Valid @RequestBody final long id) throws Exception;

    @RequestMapping(value = "/employee", produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.GET)
    ModelResult<EmployeeDto> getEmployeeById(@Valid @RequestBody final long id) throws Exception;
}
