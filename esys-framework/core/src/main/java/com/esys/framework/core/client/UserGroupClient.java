package com.esys.framework.core.client;

import com.esys.framework.core.dto.uaa.UserDto;
import com.esys.framework.core.model.ModelResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@FeignClient(value = "uaa-service",path = "/uaa/usergroup")
public interface UserGroupClient {

    @RequestMapping(value = "/{userGroupId}", method = RequestMethod.HEAD)
    ResponseEntity exist(@PathVariable("userGroupId") Long userGroupId);
}
