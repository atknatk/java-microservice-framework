package com.esys.framework.uaa.web.controller;

import com.codahale.metrics.annotation.Timed;
import com.esys.framework.core.dto.uaa.AuthorityDto;
import com.esys.framework.core.dto.uaa.RoleDto;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.core.web.controller.BaseController;
import com.esys.framework.uaa.service.IAuthorityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/authority")
public class AuthorityController extends BaseController {


    @Autowired
    private IAuthorityService authorityService;

//    @Autowired
//    private MessageSource messages;

    @GetMapping
    @Timed
    public ModelResult<List<AuthorityDto>> getAll(final HttpServletRequest request) {
        final List<AuthorityDto> list = authorityService.findAll();
        return new ModelResult.ModelResultBuilder()
                .setData(list)
                .setMessageKey("success",  request.getLocale())
                .setStatus(0)
                .build();
    }

    @Timed
    @RequestMapping(value = "/request", method = {RequestMethod.POST})
    public ResponseEntity<ModelResult> request(@RequestBody final AuthorityDto authorityDto) {
        authorityService.request(authorityDto.getName());
        ModelResult result = new ModelResult.ModelResultBuilder()
                .setMessageKey("success",  LocaleContextHolder.getLocale())
                .setStatus(0)
                .build();
        return getResponseWithData(result, HttpStatus.OK);
    }
}
