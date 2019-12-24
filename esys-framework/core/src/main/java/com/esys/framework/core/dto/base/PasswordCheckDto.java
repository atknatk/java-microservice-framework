package com.esys.framework.core.dto.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordCheckDto<T>  {
    T data;
    private String password;
}
