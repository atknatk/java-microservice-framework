package com.esys.framework.core.security.password;

import com.esys.framework.core.client.UserClient;
import com.esys.framework.core.dto.uaa.UserDto;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.exceptions.IncorrectPasswordException;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.core.security.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.util.List;

public class PasswordMethodSecurityExpressionRoot extends SecurityExpressionRoot
        implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;
    private final UserClient userClient;

    public PasswordMethodSecurityExpressionRoot(Authentication authentication,
                                                UserClient userClient) {
        super(authentication);
        this.userClient = userClient;
    }


    public boolean checkPassword(String password) {
        List<String> a = SecurityUtils.getCurrentUserAuthorities();
        UserDto u = new UserDto();
        u.setEmail(this.getPrincipal().toString());
        u.setPassword(password);
        ModelResult res = userClient.checkPassword(u).getBody();
        if(!res.isSuccess()){
            throw new IncorrectPasswordException();
        }
        return res.isSuccess();
    }


    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }

    @Override
    public void setFilterObject(Object obj) {
        this.filterObject = obj;
    }

    @Override
    public void setReturnObject(Object obj) {
        this.returnObject = obj;
    }

}
