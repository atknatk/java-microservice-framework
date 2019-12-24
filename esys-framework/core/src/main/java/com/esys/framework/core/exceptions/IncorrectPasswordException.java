package com.esys.framework.core.exceptions;

public class IncorrectPasswordException extends RuntimeException {

    private static final long serialVersionUID = 5861310537366283663L;

    public IncorrectPasswordException() {
        super("incorrectPassword");
    }

    public IncorrectPasswordException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public IncorrectPasswordException(final String message) {
        super(message);
    }

    public IncorrectPasswordException(final Throwable cause) {
        super(cause);
    }

}
