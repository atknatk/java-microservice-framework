package com.esys.framework.client.contracts.organization;

import com.esys.framework.core.dto.organization.CustomerDto;
import com.esys.framework.core.model.ModelResult;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.validation.Valid;
import java.util.List;

public interface CustomerContract {

    @RequestMapping(value = "/customer", produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.GET)
    ModelResult<List<CustomerDto>> getAllCustomers() throws Exception;


    @RequestMapping(value = "/customer",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ModelResult<CustomerDto>> saveCustomer(@Valid @RequestBody CustomerDto customerDto) throws Exception;

    @RequestMapping(value = "/customer",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ModelResult<CustomerDto>> updateCustomer(@Valid @RequestBody CustomerDto customerDto) throws Exception;

    @RequestMapping(value = "/customer",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ModelResult> deleteCustomer(@Valid @RequestBody final long id) throws Exception;

    @RequestMapping(value = "/customer", produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.GET)
    ModelResult<CustomerDto> getCustomerById(@Valid @RequestBody final long id) throws Exception;
}
