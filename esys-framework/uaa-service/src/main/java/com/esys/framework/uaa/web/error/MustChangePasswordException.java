package com.esys.framework.uaa.web.error;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

public class MustChangePasswordException extends OAuth2Exception {

    public MustChangePasswordException(String msg) {
        super(msg);
    }

    public MustChangePasswordException(String msg, Throwable t) {
        super(msg, t);
    }

    public MustChangePasswordException() {
        super("MustChangePasswordException");
    }
}
