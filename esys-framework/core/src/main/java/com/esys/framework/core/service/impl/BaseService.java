package com.esys.framework.core.service.impl;

import com.esys.framework.core.common.LongUtils;
import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.dto.base.LogEventDto;
import com.esys.framework.core.dto.uaa.RoleDto;
import com.esys.framework.core.entity.BaseEntity;
import com.esys.framework.core.entity.core.LogEvent;
import com.esys.framework.core.exceptions.AllreadyExistsException;
import com.esys.framework.core.exceptions.NullDtoException;
import com.esys.framework.core.exceptions.ResourceNotFoundException;
import com.esys.framework.core.service.IBaseService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */

public abstract class BaseService<DTO extends AbstractDto,ENTITY extends BaseEntity> implements IBaseService<DTO> {

    private final ModelMapper mapper;
    private final Logger log;
    private final Class<DTO> dtoClass;
    private final Class<ENTITY> entityClass;
    private final CrudRepository<ENTITY,Long> repository;

    public BaseService(ModelMapper mapper,Logger log,Class<DTO> dtoClass,Class<ENTITY> entityClass
        ,CrudRepository<ENTITY,Long> repository){
        this.mapper = mapper;
        this.log = log;
        this.dtoClass = dtoClass;
        this.entityClass = entityClass;
        this.repository = repository;
    }

    protected abstract boolean exist(DTO dto);


    public DTO save(DTO dto) {
        if(dto == null){
            throw new NullDtoException(dtoClass);
        }
        if(exist(dto)){
            throw new AllreadyExistsException(entityClass);
        }
        final ENTITY saved = repository.save(toEntity(dto));
        log.info("{} was saved {} ",entityClass.getSimpleName(), dto);
        return toDto(saved);
    }


    public DTO update(DTO dto) {
        if(dto == null){
            throw new NullDtoException(dtoClass);
        }
        Optional<ENTITY> entity = repository.findById(dto.getId());
        if(!entity.isPresent()){
            throw new ResourceNotFoundException(entityClass);
        }
        final ENTITY saved = repository.save(toEntity(dto));
        log.info("{} was updated {} ",entityClass.getSimpleName(), dto);
        return toDto(saved);
    }


    public DTO findById(Long id) {
        if(LongUtils.isNullOrZero(id)){
            throw new ResourceNotFoundException();
        }

        Optional<ENTITY> entity = repository.findById(id);
        if(!entity.isPresent()){
            throw new ResourceNotFoundException(entityClass);
        }

        log.info("{} was find {} ",entityClass.getSimpleName(), id);
        return toDto(entity.get());
    }

    @Override
    public List<DTO> findAll() {
        Iterable<ENTITY> all = repository.findAll();
        log.info("{} was findAll",entityClass.getSimpleName());
        return toDto(all);
    }


    public void delete(Long id) {
        if(LongUtils.isNullOrZero(id)){
            throw new ResourceNotFoundException();
        }
        repository.deleteById(id);
        log.info("{} was deleted {} ",entityClass.getSimpleName(), id);

    }


    protected ENTITY toEntity(DTO dto){
        log.debug("{} converting to {} {}",dtoClass.getName(),entityClass.getName(),dto);
        final ENTITY converted = mapper.map(dto, entityClass);
        log.debug("{} converted to {} {}",dtoClass.getName(),entityClass.getName(),converted);
        return converted;
    }

    protected List<ENTITY> toEntity(List<DTO> dto){
        log.debug("{} converting List to {} {}",dtoClass.getName(),entityClass.getName(),dto);
        final List<ENTITY> convertedList = dto.stream().map(d -> toEntity(d)).collect(Collectors.toList());
        log.debug("{} converted List to {} {}",dtoClass.getName(),entityClass.getName(),convertedList);
        return convertedList;
    }

    protected DTO toDto(ENTITY user){
        log.debug("{} converting to {} {}",entityClass.getName(),dtoClass.getName() ,user);
        final DTO converted = mapper.map(user, dtoClass);
        log.debug("{} converted to {} {}",entityClass.getName() ,dtoClass.getName(),converted);
        return converted;
    }


    protected List<DTO> toDto(Iterable<ENTITY> entities){
        log.debug("{} converting List to {} {}",entityClass.getName(),dtoClass.getName() ,entities);
        final List<DTO> convertedList = new ArrayList<>();
        entities.forEach(e -> convertedList.add(toDto(e)));
        log.debug("{} converted List to {} {}",entityClass.getName() ,dtoClass.getName(),convertedList);
        return convertedList;
    }

    protected List<DTO> toDto(List<ENTITY> entities){
        log.debug("{} converting List to {} {}",entityClass.getName(),dtoClass.getName() ,entities);
        final List<DTO> convertedList = entities.stream().map(d -> toDto(d)).collect(Collectors.toList());
        log.debug("{} converted List to {} {}",entityClass.getName() ,dtoClass.getName(),convertedList);
        return convertedList;
    }


}
