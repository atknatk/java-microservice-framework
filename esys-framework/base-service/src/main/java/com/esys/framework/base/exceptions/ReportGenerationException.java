package com.esys.framework.base.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ReportGenerationException extends RuntimeException {

    public ReportGenerationException() {
        super("reportgenerationerror");
    }

    public ReportGenerationException(Class aClass) {
        super("reportgenerationerror." + aClass.getSimpleName());
    }

    public ReportGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
    public ReportGenerationException(String message) {
        super(message);
    }
    public ReportGenerationException(Throwable cause) {
        super(cause);
    }
}
