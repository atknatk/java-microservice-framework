package com.esys.framework.message.exceptions;

import com.esys.framework.core.exceptions.GlobalExceptionHandler;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.message.consts.MessageModuleResultStatusCode;
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
public class MessageGlobalExceptionHandler extends GlobalExceptionHandler {

    @Autowired
    MessageSource messageSource;

    @ExceptionHandler(IsSameUserException.class)
    public ResponseEntity<?> allreadyExistsException(IsSameUserException ex, WebRequest request) {
        ModelResult.ModelResultBuilders builders = new ModelResult.ModelResultBuilders(messageSource,request.getLocale());
        if(!Strings.isNullOrEmpty(ex.getMessage())){
            builders.setMessageKey(ex.getMessage());
        }
        builders.setStatus(MessageModuleResultStatusCode.IsSameUser);
        saveLog(ex);
        return new ResponseEntity<>(builders.build(), BAD_REQUEST);
    }
}
