package com.esys.framework.core.web.controller;

import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.model.ModelResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.OutputStream;
import java.util.Collection;

public class BaseController {

    public ResponseEntity<ModelResult<? extends Object>> getResponseWithData(ModelResult<? extends Object> result, HttpStatus status) {
        return new ResponseEntity<>(result, status);
    }

    public ResponseEntity<ModelResult<? extends AbstractDto>> getResponseWithDto(ModelResult<? extends AbstractDto> result, HttpStatus status) {
        return new ResponseEntity<>(result, status);
    }

    public ResponseEntity<ModelResult> getResponse(ModelResult result, HttpStatus status) {
        return new ResponseEntity<>(result, status);
    }

    public ResponseEntity<ModelResult> ok(ModelResult result) {
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
