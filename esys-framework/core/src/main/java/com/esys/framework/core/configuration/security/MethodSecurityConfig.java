package com.esys.framework.core.configuration.security;

import com.esys.framework.core.client.UserClient;
import com.esys.framework.core.security.password.PasswordMethodSecurityExpressionHandler;
import com.esys.framework.core.security.password.PasswordPermissionEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class MethodSecurityConfig  extends GlobalMethodSecurityConfiguration{

    @Autowired
    private UserClient userClient;

    /**
     * Ekstra şifre istenilen metotlar için yapılan config.
     */
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        PasswordMethodSecurityExpressionHandler expressionHandler =
                new PasswordMethodSecurityExpressionHandler(userClient);
        expressionHandler.setPermissionEvaluator(new PasswordPermissionEvaluator());
        return expressionHandler;
    }

}
