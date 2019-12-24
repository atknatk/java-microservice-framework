package com.esys.framework.uaa.web.controller;

import com.codahale.metrics.annotation.Timed;
import com.esys.framework.core.client.UserGroupClient;
import com.esys.framework.core.common.LongUtils;
import com.esys.framework.core.consts.ResultStatusCode;
import com.esys.framework.core.dto.uaa.UserDto;
import com.esys.framework.core.dto.uaa.UserGroupDto;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.core.web.controller.BaseController;
import com.esys.framework.uaa.service.IUserGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/usergroup")
public class UserGroupController extends BaseController implements UserGroupClient {


    @Autowired
    private IUserGroupService service;

    @Autowired
    private MessageSource messages;

    @Timed
    @ResponseBody
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModelResult> save(final HttpServletRequest request, @Valid @RequestBody UserGroupDto userGroupDto) {
        ModelResult.ModelResultBuilders builders = new ModelResult.ModelResultBuilders(messages,request.getLocale());

        log.info("UserGroup is saving {}" , userGroupDto);
        final UserGroupDto saved = service.saveUserGroup(userGroupDto);
        log.debug("UserGroup is saved {}" , saved);
        builders.setObjectId(saved.getId());
        return getResponse(builders.success(),HttpStatus.OK);
    }


    @Timed
    @ResponseBody
    @PutMapping(value = "/{id}" ,produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModelResult> update(final HttpServletRequest request,@PathVariable("id") Long id, @Valid @RequestBody UserGroupDto userGroupDto) {
        ModelResult.ModelResultBuilders builders = new ModelResult.ModelResultBuilders(messages,request.getLocale());

        if(userGroupDto == null){
            log.warn("UserGroupDto is null");
            builders.setStatus(ResultStatusCode.RESOURCE_NOT_FOUND)
                    .setMessageKey("uaa.dto.empty");
            return getResponse(builders.build(),HttpStatus.BAD_REQUEST);
        }

        userGroupDto.setId(id);
        ModelResult result = userGroupDto.isIdEmpty(messages);
        if(!result.isSuccess()){
            log.warn("UserGroupDto is null");
            return getResponse(result,HttpStatus.BAD_REQUEST);
        }




        log.info("UserGroup is updating {}" , userGroupDto);
        final UserGroupDto saved = service.saveUserGroupIfNotExist(userGroupDto);
        log.debug("UserGroup is updated {}" , saved);

        return getResponse(builders.success(),HttpStatus.OK);
    }

    @Timed
    @ResponseBody
    @PutMapping(value = "/{id}/user" ,produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModelResult> assignUser(final HttpServletRequest request,@PathVariable("id") Long id, @Valid @RequestBody List<UserDto> list) {
        ModelResult.ModelResultBuilders builders = new ModelResult.ModelResultBuilders(messages,request.getLocale());
        service.assignUsers(id,list);
        return getResponse(builders.success(),HttpStatus.OK);
    }


    @Timed
    @DeleteMapping(  path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModelResult> delete(final HttpServletRequest request,@Valid  @PathVariable("id") Long id) {
        ModelResult.ModelResultBuilders builders = new ModelResult.ModelResultBuilders(messages,request.getLocale());

        ModelResult result = LongUtils.isNullOrZeroWithMessage(id,builders);
        if(!result.isSuccess()){
            return getResponse(result,HttpStatus.BAD_REQUEST);
        }

        log.info("UserGroup is deleting. Id : {}" , id.longValue());
        service.deleteUserGroup(id.longValue());
        log.debug("UserGroup is deleted");

        return getResponse(builders.success(),HttpStatus.OK);
    }

    @Timed
    @GetMapping(  path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModelResult<?>> getById(final HttpServletRequest request,@Valid  @PathVariable("id") Long id) {
        ModelResult.ModelResultBuilder<UserGroupDto> builders = new ModelResult.ModelResultBuilder(messages,request.getLocale());

        ModelResult result = LongUtils.isNullOrZeroWithMessage(id, builders);
        if(!result.isSuccess()){
            return getResponseWithData(result,HttpStatus.BAD_REQUEST);
        }
        log.info("Getting UserGroup. Id : {}" , id.longValue());
        Optional<UserGroupDto> groupDto = service.findOne(id.longValue());
        if(!groupDto.isPresent()){
            return getResponseWithData(builders.setMessageKey("uaa.notfound")
                    .setStatus(ResultStatusCode.RESOURCE_NOT_FOUND)
                    .build(),HttpStatus.BAD_REQUEST);
        }
        return getResponseWithData(builders.setData(groupDto.get()).build(),HttpStatus.OK);
    }

    @Timed
    @GetMapping(  path = "/{id}/user", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModelResult<List<UserDto>>> getByIdUser(final HttpServletRequest request,@Valid  @PathVariable("id") Long id) {
        ModelResult.ModelResultBuilder<List<UserDto>> builders = new ModelResult.ModelResultBuilder(messages,request.getLocale());

        ModelResult result = LongUtils.isNullOrZeroWithMessage(id, builders);
        if(!result.isSuccess()){
            return getResponseWithData(result,HttpStatus.BAD_REQUEST);
        }
        log.info("Getting UserGroup. Id : {}" , id.longValue());
        final List<UserDto> userByUserGroup = service.findUserByUserGroup(id.longValue());
        return getResponseWithData(builders.setData(userByUserGroup).build(),HttpStatus.OK);
    }


    @Timed
    @GetMapping(   produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModelResult<?>> findAll(final HttpServletRequest request) {
        ModelResult.ModelResultBuilder<List<UserGroupDto>> builders = new ModelResult.ModelResultBuilder(messages,request.getLocale());
        log.info("Fetching all user group");
        List<UserGroupDto> list  = service.findAll();
        return getResponseWithData(builders.setData(list).build(),HttpStatus.OK);
    }

    @Timed
    @GetMapping(path = "/{id}/enabled",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModelResult> setEnabled(@Valid  @PathVariable("id") Long id ) {
        log.info("user group set enabled : {}", true);
        ModelResult result = service.setEnabled(id,true);
        return getResponse(result,HttpStatus.OK);
    }

    @Timed
    @GetMapping(path = "/{id}/disabled",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModelResult> setDisabled(@Valid  @PathVariable("id") Long id ) {
        log.info("user group set disabled : {}", false);
        ModelResult result = service.setEnabled(id,false);
        return getResponse(result,HttpStatus.OK);
    }

    @Override
    @Timed
    @RequestMapping(path = "/{id}",method = RequestMethod.HEAD)
    public ResponseEntity exist(@Valid  @PathVariable("id") Long id) {
        log.info("user group is exist");
        if(service.exist(id)){
            return new ResponseEntity(HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/paging", method = RequestMethod.GET)
    public DataTablesOutput<UserGroupDto> getRoles(@Valid DataTablesInput input) {
        return service.paging(input);
    }

}
