package com.esys.framework.core.client;

import com.codahale.metrics.annotation.Timed;
import com.esys.framework.core.dto.uaa.RoleDto;
import com.esys.framework.core.model.ModelResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@FeignClient(value = "uaa-service",path = "/uaa/authority")
public interface RoleClient {

    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    ResponseEntity<ModelResult<RoleDto>> getByName(@PathVariable("name") final String name);

    @RequestMapping(value = "/name/{name}", method = RequestMethod.HEAD)
    @Timed
    ResponseEntity exist(@PathVariable("name") final String name);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    ResponseEntity<ModelResult<RoleDto>> getById(@PathVariable("id") final long id);

    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    @Timed
    ResponseEntity exist(@PathVariable("id") final long id);

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    ModelResult saveRole(@RequestBody final RoleDto roleDto);

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    ModelResult deleteRole(@PathVariable("id") final long id);

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ModelResult updateRole(@PathVariable("id") final long id ,@RequestBody final RoleDto roleDto);

}
