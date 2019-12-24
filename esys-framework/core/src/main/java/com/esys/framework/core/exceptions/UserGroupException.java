package com.esys.framework.core.exceptions;

public final class UserGroupException extends RuntimeException {

    private static final long serialVersionUID = 5861310537370282163L;

    public UserGroupException() {
        super("");
    }

    public UserGroupException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserGroupException(final String message) {
        super(message);
    }

    public UserGroupException(final Throwable cause) {
        super(cause);
    }

}
