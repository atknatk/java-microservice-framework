package com.esys.framework.organization.web.controller;

import com.esys.framework.client.contracts.organization.EmployeeContract;
import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.dto.organization.CustomerDto;
import com.esys.framework.core.dto.organization.EmployeeDto;
import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.entity.BaseEntity;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.organization.service.ICustomerService;
import com.esys.framework.organization.service.IEmployeeService;
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
@RequestMapping("employee")
public class EmployeeController implements EmployeeContract {

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ModelResult<EmployeeDto>> saveEmployee(@Valid @RequestBody EmployeeDto employeeDto) throws Exception {
        log.debug("REST request to save Employee : {}", employeeDto);
        ModelResult<EmployeeDto> result =  employeeService.save(employeeDto);
        return ResponseEntity.created(new URI("/api/employee/" + result.getData().getId()))
                .body(result);
    }

    @RequestMapping(method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModelResult<EmployeeDto>> updateEmployee(@Valid @RequestBody EmployeeDto employeeDto) throws Exception {
        ModelResult<EmployeeDto> result =  employeeService.update(employeeDto);
        return ResponseEntity.created(new URI("/api/employee/" + result.getData().getId()))
                .body(result);
    }

    @RequestMapping(method = RequestMethod.DELETE,
            path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModelResult> deleteEmployee(@Valid  @PathVariable("id") long id) throws Exception {
        ModelResult result =  employeeService.delete(id);
        return ResponseEntity.created(new URI("/api/employee/" + result.getData()))
                .body(result);
    }

    @GetMapping(path = "/{id}",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ModelResult<EmployeeDto> getEmployeeById(@Valid  @PathVariable("id")  long id) throws Exception {
        ModelResult<EmployeeDto> result = employeeService.findOne(id);
        return result;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ModelResult<List<EmployeeDto>> getAllEmployees() throws Exception {

        ModelResult<List<EmployeeDto>> list = employeeService.getAll();
        return list;
    }

    @GetMapping(path = "/{id}/customer",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ModelResult<List<CustomerDto>> findCustomersByEmployee(@Valid @PathVariable("id") long id) throws Exception {
        ModelResult<List<CustomerDto>> result =  customerService.findCustomersByEmployee(id);
        return result;
    }

       @RequestMapping(value = "/{id}/user", method = RequestMethod.GET)
    public List<BasicUserDto> getUsers(@PathVariable("id") Long id) {
        return employeeService.users(id);
    }

    @RequestMapping(value = "/{id}/user", method = RequestMethod.POST)
    public ResponseEntity saveUsers(@PathVariable("id") Long id,
                                    @RequestBody List<User> users) {
        employeeService.saveUsers(id, users);
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "/{id}/user/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        employeeService.delete(id, new User(userId));
        return ResponseEntity.ok(null);
    }


}
