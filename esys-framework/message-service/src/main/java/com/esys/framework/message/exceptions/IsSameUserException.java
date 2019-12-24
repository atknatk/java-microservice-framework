package com.esys.framework.message.exceptions;

public final class IsSameUserException extends RuntimeException {

    private static final long serialVersionUID = 5861310537512882163L;

    public IsSameUserException() {
        super("isSameUser");
    }

    public IsSameUserException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public IsSameUserException(final String message) {
        super(message);
    }

    public IsSameUserException(final Throwable cause) {
        super(cause);
    }

}
