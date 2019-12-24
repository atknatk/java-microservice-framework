package com.esys.framework.base.exceptions;

import com.esys.framework.base.consts.BaseResultStatusCode;
import com.esys.framework.core.exceptions.GlobalExceptionHandler;
import com.esys.framework.core.model.ModelResult;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ControllerAdvice
public class GlobalBaseExceptionHandler extends GlobalExceptionHandler {

    @Autowired
    MessageSource messageSource;

    @ExceptionHandler(ReportGenerationException.class)
    public ResponseEntity<?> reportGenerationException(ReportGenerationException ex, WebRequest request) {
        ModelResult.ModelResultBuilders builders = new ModelResult.ModelResultBuilders(messageSource,request.getLocale());
        if(!Strings.isNullOrEmpty(ex.getMessage())){
            builders.setMessageKey(ex.getMessage());
        }
        builders.setStatus(BaseResultStatusCode.ReportGenerationError);
        saveLog(ex);
        return new ResponseEntity<>(builders.build(), BAD_REQUEST);
    }

}
