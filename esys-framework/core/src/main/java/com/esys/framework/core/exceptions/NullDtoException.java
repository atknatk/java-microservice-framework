package com.esys.framework.core.exceptions;

public final class NullDtoException extends RuntimeException {

    private static final long serialVersionUID = 5861730537366282163L;

    public NullDtoException() {
        super("nulldto");
    }

    public NullDtoException(Class aClass) {
        super("nulldto." + aClass.getSimpleName());
    }

    public NullDtoException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NullDtoException(final String message) {
        super(message);
    }

    public NullDtoException(final Throwable cause) {
        super(cause);
    }

}
