package com.esys.framework.uaa.web.error;

import com.esys.framework.core.model.ModelResult;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

@JsonSerialize(using = CustomOauthExceptionSerializer.class)
public class CustomOauthException extends OAuth2Exception {

    @Getter
    private ModelResult result;

    public CustomOauthException(String msg,ModelResult modelResult) {
        super(msg);
        result = modelResult;
    }
}