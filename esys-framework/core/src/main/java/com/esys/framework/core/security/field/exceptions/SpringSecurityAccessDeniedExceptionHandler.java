package com.esys.framework.core.security.field.exceptions;

import org.springframework.security.access.AccessDeniedException;

import javax.validation.constraints.NotNull;

public class SpringSecurityAccessDeniedExceptionHandler implements AccessDeniedExceptionHandler{
    @Override
    public boolean permitAccess(@NotNull Exception e) throws Exception {
        if(e instanceof AccessDeniedException){
            return false;
            // throw e;
        }
        return false;
    }
}
