package com.esys.framework.core.converter;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

public  abstract class MappingConfigurationConfigurer {
    void configureImpl(ModelMapper mapper) {
        configure(mapper.getConfiguration());
    }

    public abstract void configure(Configuration configuration);
}
