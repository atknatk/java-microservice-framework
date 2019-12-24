package com.esys.framework.core.security.field.exceptions;

import javax.validation.constraints.NotNull;

public interface AccessDeniedExceptionHandler {

    boolean permitAccess(@NotNull Exception var1) throws Exception;

}
