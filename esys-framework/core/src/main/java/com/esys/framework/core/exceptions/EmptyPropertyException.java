package com.esys.framework.core.exceptions;

public class EmptyPropertyException extends Exception {

    public EmptyPropertyException() {
        super();
    }

    public EmptyPropertyException(String message) {
        super(message);
    }

    public EmptyPropertyException(String message, Throwable cause) {
        super(message, cause);
    }
}
