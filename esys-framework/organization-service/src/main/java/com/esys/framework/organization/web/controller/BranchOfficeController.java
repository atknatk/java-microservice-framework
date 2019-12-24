package com.esys.framework.organization.web.controller;

import com.esys.framework.client.contracts.organization.BranchOfficeContract;
import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.dto.organization.BranchOfficeDto;
import com.esys.framework.core.dto.organization.EmployeeDto;
import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.entity.BaseEntity;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.organization.service.IBranchOfficeService;
import com.esys.framework.organization.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("branchoffice")
public class BranchOfficeController implements BranchOfficeContract {

    @Autowired
    private IBranchOfficeService branchOfficeService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ModelResult<BranchOfficeDto>> saveBranchOffice(@Valid @RequestBody BranchOfficeDto branchOfficeDto) throws Exception {
        log.debug("REST request to save Branch Office : {}", branchOfficeDto);
        ModelResult<BranchOfficeDto> result =  branchOfficeService.save(branchOfficeDto);
        return ResponseEntity.created(new URI("/api/branchoffice/" + result.getData().getId()))
                //.headers(HeaderUtil.createEntityCreationAlert("device", result.getId().toString()))
                .body(result);
    }

    @RequestMapping(method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModelResult<BranchOfficeDto>> updateBranchOffice(@Valid @RequestBody BranchOfficeDto branchOfficeDto) throws Exception {
        ModelResult<BranchOfficeDto> result =  branchOfficeService.update(branchOfficeDto);
        return ResponseEntity.created(new URI("/api/branchOffice/" + result.getData().getId()))
                .body(result);
    }

    @RequestMapping(method = RequestMethod.DELETE,
            path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModelResult> deleteBranchOffice(@Valid  @PathVariable("id")  long id) throws Exception {
        ModelResult result =  branchOfficeService.delete(id);
        return ResponseEntity.created(new URI("/api/branchOffice/" + result.getData()))
                .body(result);
    }

    @GetMapping(path = "/{id}",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ModelResult<BranchOfficeDto> getBranchOfficeById(@Valid @PathVariable("id") long id) throws Exception {
        ModelResult<BranchOfficeDto> result = branchOfficeService.findOne(id);
        return result;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ModelResult<List<BranchOfficeDto>> getAllBranchOffices() throws Exception {

        ModelResult<List<BranchOfficeDto>> list = branchOfficeService.getAll();
        return list;
    }

    @GetMapping(path = "/{id}/employee",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ModelResult<List<EmployeeDto>> findEmployeesByDepartment(@Valid @PathVariable("id") long id) throws Exception {
        ModelResult<List<EmployeeDto>> result =  employeeService.findEmployeesByDepartment(id);
        return result;
    }

    private <D extends AbstractDto,E extends BaseEntity> D convertToEntity(E entity, Class<D> clazz) {
        try{
            return (D) modelMapper.map(entity, clazz);
        }catch (Exception e){
            log.error("Parse Error",e);
        }
        return null;
    }

    @RequestMapping(value = "/{id}/user", method = RequestMethod.GET)
    public List<BasicUserDto> getUsers(@PathVariable("id") Long id) {
        return branchOfficeService.users(id);
    }

    @RequestMapping(value = "/{id}/user", method = RequestMethod.POST)
    public ResponseEntity saveUsers(@PathVariable("id") Long id,
                                    @RequestBody List<User> users) {
        branchOfficeService.saveUsers(id, users);
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "/{id}/user/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        branchOfficeService.delete(id, new User(userId));
        return ResponseEntity.ok(null);
    }
}
