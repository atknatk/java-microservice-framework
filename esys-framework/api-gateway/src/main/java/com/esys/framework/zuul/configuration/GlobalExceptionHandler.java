package com.esys.framework.zuul.configuration;

import com.esys.framework.zuul.model.ModelResult;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ZuulException.class)
    public ResponseEntity<?> resourceNotFoundException(ZuulException ex, WebRequest request) {
        ModelResult result = new ModelResult.ModelResultBuilders()
                .setMessageKey(ex.getMessage())
                .setMessage(ex.getLocalizedMessage())
                .addError(ex.getMessage())
                .setStatus(-1)
                .build();
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exception(ZuulException ex, WebRequest request) {
        ModelResult result = new ModelResult.ModelResultBuilders()
                .setMessageKey(ex.getMessage())
                .setMessage(ex.getLocalizedMessage())
                .addError(ex.getMessage())
                .setStatus(-1)
                .build();
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}