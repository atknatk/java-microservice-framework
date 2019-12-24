package com.esys.framework.core.configuration.security;

import com.esys.framework.core.security.field.policy.AuthorityBasedFieldSecurityPolicy;
import com.esys.framework.core.security.field.policy.CreatedByFieldSecurityPolicy;
import com.esys.framework.core.security.field.policy.RoleBasedFieldSecurityPolicy;
import com.esys.framework.core.security.field.principal.PrincipalProvider;
import org.ehcache.core.spi.store.tiering.AuthoritativeTier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@ConditionalOnClass(SecurityContextHolder.class)
public class SecureFieldPolicyAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean(name = "createdByFieldSecurityPolicy")
    public CreatedByFieldSecurityPolicy createdByFieldSecurityPolicy(){
        return new CreatedByFieldSecurityPolicy();
    }

    @Bean
    @ConditionalOnMissingBean(name = "roleBasedFieldSecurityPolicy")
    public RoleBasedFieldSecurityPolicy roleBasedFieldSecurityPolicy(PrincipalProvider principalProvider){
        return new RoleBasedFieldSecurityPolicy(principalProvider);
    }

    @Bean
    @ConditionalOnMissingBean(name = "authorityBasedFieldSecurityPolicy")
    public AuthorityBasedFieldSecurityPolicy authorityBasedFieldSecurityPolicy(PrincipalProvider principalProvider){
        return new AuthorityBasedFieldSecurityPolicy(principalProvider);
    }


}
