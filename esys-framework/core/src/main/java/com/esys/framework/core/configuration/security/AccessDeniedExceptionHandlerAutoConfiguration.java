package com.esys.framework.core.configuration.security;

import com.esys.framework.core.security.field.exceptions.AccessDeniedExceptionHandler;
import com.esys.framework.core.security.field.exceptions.SpringSecurityAccessDeniedExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.AccessDeniedException;

@Configuration
@ConditionalOnClass(AccessDeniedException.class)
public class AccessDeniedExceptionHandlerAutoConfiguration {

    /**
     * Access Denied oldugu zaman handle etmek için gerekli konfigurasyon
     * @return
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(AccessDeniedExceptionHandler.class)
    public SpringSecurityAccessDeniedExceptionHandler accessDeniedExceptionHandler(){
      return new SpringSecurityAccessDeniedExceptionHandler();
    }
}
