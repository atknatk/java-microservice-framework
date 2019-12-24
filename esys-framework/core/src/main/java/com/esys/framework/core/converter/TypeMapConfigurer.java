package com.esys.framework.core.converter;


import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.entity.BaseEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration;
import org.modelmapper.internal.typetools.TypeResolver;

public abstract class TypeMapConfigurer<S extends BaseEntity, D extends AbstractDto> {

    public String getTypeMapName() {
        return null;
    }

    public Configuration getConfiguration() {
        return null;
    }

    public abstract void toDto(TypeMap<S, D> typeMap);

    public abstract void toEntity(TypeMap<D, S> typeMap);

    @SuppressWarnings("unchecked")
    /**
     * Model Mapper configurasyonlarini okuyup modelMapper'a config olarak ileten config.
     */
    void configureImpl(ModelMapper mapper) {
        Class<?>[] typeArguments = TypeResolver.resolveRawArguments(TypeMapConfigurer.class, getClass());
        String typeMapName = getTypeMapName();
        Configuration configuration = getConfiguration();

        if (typeMapName == null && configuration == null) {
            toDto(mapper.createTypeMap((Class<S>) typeArguments[0], (Class<D>) typeArguments[1]));
            toEntity(mapper.createTypeMap((Class<D>) typeArguments[1], (Class<S>) typeArguments[0]));
        } else if (typeMapName == null) {
            toDto(mapper.createTypeMap((Class<S>) typeArguments[0], (Class<D>) typeArguments[1], configuration));
            toEntity(mapper.createTypeMap((Class<D>) typeArguments[1], (Class<S>) typeArguments[0], configuration));
        } else if (configuration == null) {
            toDto(mapper.createTypeMap((Class<S>) typeArguments[0], (Class<D>) typeArguments[1], typeMapName));
            toEntity(mapper.createTypeMap((Class<D>) typeArguments[1], (Class<S>) typeArguments[0], typeMapName));
        } else {
            toDto(mapper.createTypeMap((Class<S>) typeArguments[0], (Class<D>) typeArguments[1], typeMapName, configuration));
            toEntity(mapper.createTypeMap((Class<D>) typeArguments[1], (Class<S>) typeArguments[0], typeMapName, configuration));
        }
    }
}
