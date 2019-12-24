package com.esys.framework.core.exceptions;

public final class AllreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 5861310537366282163L;

    public AllreadyExistsException() {
        super("allreadyexists");
    }

    public AllreadyExistsException(Class aClass) {
        super("allreadyexists." + aClass.getSimpleName());
    }

    public AllreadyExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AllreadyExistsException(final String message) {
        super(message);
    }

    public AllreadyExistsException(final Throwable cause) {
        super(cause);
    }

}
