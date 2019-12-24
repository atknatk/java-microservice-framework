package com.esys.framework.core.client;

import com.esys.framework.core.dto.uaa.UserDto;
import com.esys.framework.core.model.ModelResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@FeignClient(value = "uaa-service",path = "/uaa/user")
public interface UserClient {

    @RequestMapping(value = "/checkPassword", method = RequestMethod.POST)
    ResponseEntity<ModelResult> checkPassword(UserDto user);

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    ResponseEntity<ModelResult<UserDto>> getUserById(final Locale locale, @PathVariable("userId") Long userId);

    @RequestMapping(method = RequestMethod.GET, value = "/session")
    ModelResult<UserDto> session(@RequestHeader("Authorization") String token);

    @RequestMapping(method = RequestMethod.GET)
    ModelResult<List<UserDto>> getAllUser();

    @RequestMapping(value = "/{userId}/role", method = RequestMethod.POST)
    @ResponseBody
    ModelResult assignRoles(@PathVariable("userId") Long userId, @RequestBody List<String> roles);

    @RequestMapping(value = "/{userId}/role/{roleId}", method = RequestMethod.POST)
    @ResponseBody
    ModelResult assignRole(@PathVariable("userId") Long userId, @PathVariable("roleId") Long roleId);

    @RequestMapping(value = "/{userId}/role/{roleId}", method = RequestMethod.DELETE)
    @ResponseBody
    ModelResult removeRole(@PathVariable("userId") Long userId, @PathVariable("roleId") Long roleId);

    @RequestMapping(value = "/{userId}", method = RequestMethod.HEAD)
    ResponseEntity exist(@PathVariable("userId") Long userId);
}
