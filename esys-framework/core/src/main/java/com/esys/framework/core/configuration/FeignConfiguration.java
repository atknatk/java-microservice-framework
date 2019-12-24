package com.esys.framework.core.configuration;

import com.esys.framework.core.feign.UserFeignClientInterceptor;
import feign.RequestInterceptor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {
        "com.esys.framework.client.service",
        "com.esys.framework.core.client"})
public class FeignConfiguration {

    /**
     * Feign configurasyonlarini intercept edebilmesi icin gerekli config.
     */
    @Bean
    public RequestInterceptor getUserFeignClientInterceptor() {
        return new UserFeignClientInterceptor();
    }
}
