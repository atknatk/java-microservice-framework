package com.esys.framework.organization.web.controller;

import com.esys.framework.client.contracts.organization.GroupContract;
import com.esys.framework.core.dto.organization.CompanyDto;
import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.dto.uaa.UserDto;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.dto.organization.GroupDto;
import com.esys.framework.core.entity.BaseEntity;
import com.esys.framework.organization.service.ICompanyService;
import com.esys.framework.organization.service.ICustomerService;
import com.esys.framework.organization.service.IGroupService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("group")
public class GroupController implements GroupContract {



    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IGroupService groupService;

    @Autowired
    private ICompanyService companyService ;



    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ModelResult<GroupDto>> saveGroup(@Valid @RequestBody GroupDto groupDto) throws Exception {
        log.debug("REST request to save Group : {}", groupDto);
        ModelResult<GroupDto> result =  groupService.save(groupDto);
        return ResponseEntity.created(new URI("/api/group/" + result.getData().getId()))
                .body(result);
    }

    @RequestMapping(method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModelResult<GroupDto>> updateGroup(@Valid @RequestBody GroupDto groupDto) throws Exception {
        ModelResult<GroupDto> result =  groupService.update(groupDto);
        return ResponseEntity.created(new URI("/api/group/" + result.getData().getId()))
                .body(result);
    }

    @RequestMapping(method = RequestMethod.DELETE,
            path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModelResult> deleteGroup(@Valid @PathVariable("id") long id) throws Exception {
        ModelResult result =  groupService.delete(id);
        return ResponseEntity.created(new URI("/api/group/" + result.getData()))
                .body(result);
    }

    @GetMapping(path = "/{id}",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ModelResult<GroupDto> getGroupById(@Valid @PathVariable("id") long id) throws Exception {
        ModelResult<GroupDto> result = groupService.findOne(id);
        return result;

    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ModelResult<List<GroupDto>> getAllGroups() {

        ModelResult<List<GroupDto>> list = groupService.getAll();
        return list;
    }

    @GetMapping(path = "/{id}/company",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ModelResult<List<CompanyDto>> findCompaniesByGroupId(@Valid @PathVariable("id") long id) throws Exception {
        ModelResult<List<CompanyDto>> result =  companyService.findCompaniesByGroupId(id);
        return result;
    }


    @RequestMapping(value = "/{id}/user", method = RequestMethod.GET)
    public List<BasicUserDto> getUsers(@PathVariable("id") Long id) {
        return groupService.users(id);
    }

    @RequestMapping(value = "/{id}/user", method = RequestMethod.POST)
    public ResponseEntity saveUsers(@PathVariable("id") Long id,
                                    @RequestBody List<User> users) {
        groupService.saveUsers(id, users);
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "/{id}/user/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        groupService.delete(id, new User(userId));
        return ResponseEntity.ok(null);
    }

    private <D extends AbstractDto,E extends BaseEntity> D convertToEntity(E entity, Class<D> clazz) {
        try{
            return (D) modelMapper.map(entity, clazz);
        }catch (Exception e){
            log.error("Parse Error",e);
        }
        return null;
    }

}
