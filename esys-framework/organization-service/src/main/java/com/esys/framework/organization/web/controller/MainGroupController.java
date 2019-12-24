package com.esys.framework.organization.web.controller;


import com.esys.framework.client.contracts.organization.MainGroupContract;
import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.dto.base.PasswordCheckDto;
import com.esys.framework.core.dto.organization.GroupDto;
import com.esys.framework.core.dto.organization.MainGroupDto;
import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.entity.BaseEntity;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.organization.service.IGroupService;
import com.esys.framework.organization.service.IMainGroupService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("maingroup")
public class MainGroupController  implements MainGroupContract  {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IMainGroupService mainGroupService;

    @Autowired
    private IGroupService groupService;

    @RequestMapping(method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModelResult<MainGroupDto>> saveMainGroup(@Valid @RequestBody MainGroupDto mainGroupDto) throws Exception {
        ModelResult<MainGroupDto> result =  mainGroupService.save(mainGroupDto);
        return ResponseEntity.created(new URI("/api/maingroups/" + result.getData().getId()))
                    .body(result);

    }

    @RequestMapping(method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("checkPassword(#dto.password)")
    public ResponseEntity<ModelResult<MainGroupDto>> updateMainGroup(
            @Valid @RequestBody PasswordCheckDto<MainGroupDto> dto) throws Exception {
        ModelResult<MainGroupDto> result =  mainGroupService.update(dto.getData());
        return ResponseEntity.created(new URI("/api/maingroups/" + result.getData().getId()))
                .body(result);
    }

    @RequestMapping(method = RequestMethod.DELETE,
            path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModelResult> deleteMainGroup(@Valid @PathVariable("id") long id) throws Exception {
        ModelResult result =  mainGroupService.delete(id);
        return ResponseEntity.created(new URI("/api/maingroups/" + result.getData()))
                .body(result);
    }

    @GetMapping(path = "/{id}",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ModelResult<MainGroupDto> getMainGroupById(@Valid @PathVariable("id") long id) throws Exception {
        ModelResult<MainGroupDto> result =  mainGroupService.findOne(id);
        return result;
    }


    @GetMapping(path = "/{id}/group",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ModelResult<List<GroupDto>> getGroupByMainGroupId(@Valid @PathVariable("id") long id) throws Exception {
        ModelResult<List<GroupDto>> result =  groupService.findGroupsByMainGroup(id);
        return result;
    }


    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ModelResult<List<MainGroupDto>> getAllMainGroups() throws URISyntaxException {

        ModelResult<List<MainGroupDto>> list =  mainGroupService.getAll();
        return list;
    }


    @RequestMapping(value = "/{id}/user", method = RequestMethod.GET)
    public List<BasicUserDto> getUsers(@PathVariable("id") Long id) {
        return mainGroupService.users(id);
    }


    @RequestMapping(value = "/{id}/user", method = RequestMethod.POST)
    public ResponseEntity saveUsers(@PathVariable("id") Long id,
                                       @RequestBody List<User> users) {
        mainGroupService.saveUsers(id, users);
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "/{id}/user/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity saveUsers(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        mainGroupService.delete(id, new User(userId));
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
