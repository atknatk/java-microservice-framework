package com.esys.framework.core.configuration.mapping;

import com.esys.framework.core.converter.MappingConfigurationConfigurer;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MappingConfiguration extends MappingConfigurationConfigurer {

    /**
     * Model mapper konfigurasyonu
     * Model null olduğu zaman gormezden gelir
     * @param configuration modelmapper.config.Configuration
     */
    @Override
    public void configure(org.modelmapper.config.Configuration configuration) {
        configuration.setSkipNullEnabled(true);
        configuration.setMatchingStrategy(MatchingStrategies.STRICT);
    }
}
