package com.esys.framework.core.configuration.security;

import com.esys.framework.core.security.field.entity.EntityCreatedByProvider;
import com.esys.framework.core.security.field.entity.SpringDataEntityCreatedByProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.annotation.CreatedBy;

@Configuration
@ConditionalOnClass(CreatedBy.class)
public class EntityCreatedByProviderAutoConfiguration {

    @Bean
    @Primary
    @ConditionalOnMissingBean(EntityCreatedByProvider.class)
    public SpringDataEntityCreatedByProvider entityCreatedByProvider(){
        return new SpringDataEntityCreatedByProvider();
    }

}
