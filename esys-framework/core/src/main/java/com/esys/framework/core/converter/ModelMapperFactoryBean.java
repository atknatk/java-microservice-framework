package com.esys.framework.core.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class ModelMapperFactoryBean implements FactoryBean<ModelMapper> {


    @Autowired(required = false)
    private MappingConfigurationConfigurer mapperConfigurer;


    @Autowired(required = false)
    private List<TypeMapConfigurer> configurers;

    @Autowired(required = false)
    private List<MappingConverterConfigurer> converters;

     @Override
    public ModelMapper getObject() throws Exception {
        final ModelMapper modelMapper = new ModelMapper();
        configure(modelMapper);
        return modelMapper;
    }


    @Override
    public Class<?> getObjectType() {
        return ModelMapper.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private void configure(ModelMapper modelMapper) {
        if (mapperConfigurer != null) {
            mapperConfigurer.configureImpl(modelMapper);
        }

        if (converters != null) {
            converters.forEach(typeMapConfigurer -> typeMapConfigurer.configureImpl(modelMapper));
        }

        if (configurers != null) {
            configurers.forEach(typeMapConfigurer -> typeMapConfigurer.configureImpl(modelMapper));
        }
    }
}