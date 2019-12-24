package com.esys.framework.core.consts;

public interface ResultStatusCode {

    int UNKNOWN_ERROR = -1;
    int SUCCESS = 0;
    int UNAUTHORIZED = 1;
    int RESOURCE_NOT_FOUND = 2;
    int RECAPTCHA_UNAVAILABLE = 3;
    int RECAPTCHA_INVALID = 4;
    int INVALID_GRANT = 5;
    int USER_DISABLED = 6;
    int INVALID_REQUEST = 8;
    int INVALID_CLIENT = 9;
    int UNAUTHORIZED_CLIENT = 10;
    int UNSUPPORTED_GRANT_TYPE = 11;
    int INVALID_SCOPE = 12;
    int INSUFFICIENT_SCOPE = 13;
    int INVALID_TOKEN = 14;
    int REDIRECT_URI_MISMATCH = 15;
    int UNSUPPORTED_RESPONSE_TYPE = 16;
    int ACCESS_DENIED = 17;
    int MUST_CHANGE_PASSWORD = 18;
    int ALLREADY_EXISTS = 19;
    int DTO_NOT_VALID = 20;
    int DATA_NOT_FOUND = 21;
    int INCORRECT_PASSWORD = 22;
    int FORBIDDEN = 23;
    int USERGROUP_NOT_ACTIVE = 24;
}
