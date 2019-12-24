package com.esys.framework.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.application")
@Configuration("applicationProperties")
public class ApplicationProperties {

    private String name;
}
