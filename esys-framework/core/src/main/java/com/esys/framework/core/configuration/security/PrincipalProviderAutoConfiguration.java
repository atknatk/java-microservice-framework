package com.esys.framework.core.configuration.security;

import com.esys.framework.core.security.field.principal.PrincipalProvider;
import com.esys.framework.core.security.field.principal.SpringSecurityPrincipalProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@ConditionalOnClass(SecurityContextHolder.class)
public class PrincipalProviderAutoConfiguration {

    @Bean
    @Primary
    @ConditionalOnMissingBean(PrincipalProvider.class)
    public SpringSecurityPrincipalProvider principalProvider(){
        return new SpringSecurityPrincipalProvider();
    }

}
