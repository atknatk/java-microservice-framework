package com.esys.framework.organization.web.controller;

import com.esys.framework.client.contracts.organization.CustomerContract;
import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.dto.organization.CustomerDto;
import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.entity.BaseEntity;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.organization.service.ICustomerService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("customer")
public class CustomerController implements CustomerContract {

    @Autowired
    private ICustomerService customerService;


    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ModelResult<CustomerDto>> saveCustomer(@Valid @RequestBody CustomerDto customerDto) throws Exception {
        log.debug("REST request to save Customer : {}", customerDto);
        ModelResult<CustomerDto> result =  customerService.save(customerDto);
        return ResponseEntity.created(new URI("/api/customer/" + result.getData().getId()))
                .body(result);
    }

    @RequestMapping(method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModelResult<CustomerDto>> updateCustomer(@Valid @RequestBody CustomerDto customerDto) throws Exception {
        ModelResult<CustomerDto> result =  customerService.update(customerDto);
        return ResponseEntity.created(new URI("/api/customer/" + result.getData().getId()))
                .body(result);
    }

    @RequestMapping(method = RequestMethod.DELETE,
            path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModelResult> deleteCustomer(@Valid  @PathVariable("id") long id) throws Exception {
        ModelResult result =  customerService.delete(id);
        return ResponseEntity.created(new URI("/api/customer/" + result.getData()))
                .body(result);
    }

    @GetMapping(path = "/{id}",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ModelResult<CustomerDto> getCustomerById(@Valid  @PathVariable("id")  long id) throws Exception {
        ModelResult<CustomerDto> result = customerService.findOne(id);
        return result;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ModelResult<List<CustomerDto>> getAllCustomers() throws Exception {

        ModelResult<List<CustomerDto>> list = customerService.getAll();
        return list;
    }

    @RequestMapping(value = "/{id}/user", method = RequestMethod.GET)
    public List<BasicUserDto> getUsers(@PathVariable("id") Long id) {
        return customerService.users(id);
    }


    @RequestMapping(value = "/{id}/user", method = RequestMethod.POST)
    public ResponseEntity saveUsers(@PathVariable("id") Long id,
                                    @RequestBody List<User> users) {
        customerService.saveUsers(id, users);
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "/{id}/user/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        customerService.delete(id, new User(userId));
        return ResponseEntity.ok(null);
    }
}
