package com.esys.framework.core.converter;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

public abstract class MappingConverterConfigurer<S, D> {

    public abstract Converter<S, D> converter();

    @SuppressWarnings("unchecked")
    void configureImpl(ModelMapper mapper) {
        mapper.addConverter(converter());
    }
}