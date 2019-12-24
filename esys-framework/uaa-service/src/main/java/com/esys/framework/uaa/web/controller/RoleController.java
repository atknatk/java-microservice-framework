package com.esys.framework.uaa.web.controller;

import com.codahale.metrics.annotation.Timed;
import com.esys.framework.core.client.RoleClient;
import com.esys.framework.core.dto.uaa.AuthorityDto;
import com.esys.framework.core.dto.uaa.RoleDto;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.core.web.controller.BaseController;
import com.esys.framework.uaa.service.IAuthorityService;
import com.esys.framework.uaa.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController implements RoleClient {


    private final IRoleService roleService;

    private final IAuthorityService authorityService;

    @Autowired
    public RoleController(IRoleService roleService, IAuthorityService authorityService) {
        this.roleService = roleService;
        this.authorityService = authorityService;
    }

    @GetMapping
    @Timed
    public ModelResult<List<RoleDto>> getAll(final HttpServletRequest request) {
        final List<RoleDto> list = roleService.findAll();
        return new ModelResult.ModelResultBuilder<List<RoleDto>>()
                .setData(list)
                .setMessageKey("success",  request.getLocale())
                .setStatus(0)
                .build();
    }

    @PostMapping
    @Timed
    @ResponseBody
    public ModelResult<RoleDto> saveRole(@RequestBody final RoleDto roleDto) {
        final RoleDto role = roleService.saveRole(roleDto);
        return new ModelResult.ModelResultBuilder()
                .setData(role,LocaleContextHolder.getLocale())
                .build();
    }

    @DeleteMapping("/{id}")
    @Timed
    @ResponseBody
    public ModelResult deleteRole(@PathVariable("id") final long id) {
        final boolean success = roleService.deleteRole(id);
        return new ModelResult.ModelResultBuilders()
                .setStatus(success ? 0 : -1)
                .build();
    }

    @PutMapping("/{id}")
    @Timed
    @ResponseBody
    public ModelResult updateRole(@PathVariable("id") final long id ,
                                  @RequestBody final RoleDto roleDto) {
        roleDto.setId(id);
        final RoleDto role = roleService.updateRole(roleDto);
        return new ModelResult.ModelResultBuilder()
                .setData(role,LocaleContextHolder.getLocale())
                .build();
    }



    @GetMapping("/{id}/authority")
    @Timed
    public ModelResult<List<AuthorityDto>> getAuthoritiesByRoleId(final HttpServletRequest request, @PathVariable("id") final long id) {
        final List<AuthorityDto> list = authorityService.getAuthoritiesByRoleId(id);
        return new ModelResult.ModelResultBuilder()
                .setData(list)
                .setMessageKey("success",  request.getLocale())
                .setStatus(0)
                .build();
    }


    @RequestMapping(value = "/{id}", method = {RequestMethod.GET, RequestMethod.HEAD})
    @Timed
    public ResponseEntity<ModelResult<RoleDto>> getById(@PathVariable("id") final long id) {
        RoleDto roleDto= roleService.getById(id);
        if(roleDto == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
            ModelResult result = new ModelResult.ModelResultBuilder()
                    .setData(roleDto)
                    .setMessageKey("success",  LocaleContextHolder.getLocale())
                    .setStatus(0)
                    .build();
        return getResponseWithData(result,HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    @Timed
    public ResponseEntity exist(@PathVariable("id") final long id) {
        RoleDto roleDto= roleService.getById(id);
        if(roleDto == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    @Timed
    public ResponseEntity<ModelResult<RoleDto>> getByName(@PathVariable("name") final String name) {
        RoleDto roleDto= roleService.getByName(name);
        if(roleDto == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ModelResult result = new ModelResult.ModelResultBuilder()
                .setData(roleDto)
                .setMessageKey("success",  LocaleContextHolder.getLocale())
                .setStatus(0)
                .build();
        return getResponseWithData(result,HttpStatus.OK);
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.HEAD)
    @Timed
    public ResponseEntity exist(@PathVariable("name") final String name) {
        RoleDto roleDto= roleService.getByName(name);
        if(roleDto == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(value = "/paging", method = RequestMethod.GET)
    public DataTablesOutput<RoleDto> getRoles(@Valid DataTablesInput input) {
        return roleService.rolesPaging(input);
    }

}
