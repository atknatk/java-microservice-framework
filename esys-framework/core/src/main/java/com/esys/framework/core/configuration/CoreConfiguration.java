package com.esys.framework.core.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "com.esys.framework.core"
})
public class CoreConfiguration {
    @Bean
    public EsysProperties esysProperties(){
        return new EsysProperties();
    }

}
