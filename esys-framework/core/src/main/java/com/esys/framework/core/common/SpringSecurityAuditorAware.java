package com.esys.framework.core.common;

import com.esys.framework.core.consts.Constants;
import com.esys.framework.core.security.SecurityUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        String userName = SecurityUtils.getCurrentUserLogin();
        return (userName != null ? Optional.of(userName) : Optional.of(Constants.SYSTEM_ACCOUNT));
    }
}
