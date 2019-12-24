package com.esys.framework.core.security.field.principal;

import com.esys.framework.core.security.SecurityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Nullable;
import java.util.List;

public class SpringSecurityPrincipalProvider implements PrincipalProvider{

    @Nullable
    @Override
    public String getPrincipal() {
        return SecurityUtils.getCurrentUserLogin();
    }

    @Nullable
    @Override
    public List<String> getRoles() {
        return SecurityUtils.getCurrentUserRoles();
    }

    @Nullable
    @Override
    public List<String> getAuthorities() {
        return SecurityUtils.getCurrentUserAuthorities();
    }
}
