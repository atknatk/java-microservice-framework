package com.esys.framework.core.service.impl;

import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.dto.organization.MainGroupDto;
import com.esys.framework.core.entity.BaseEntity;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.core.service.IGenericCrudService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public abstract class GenericCrudService<T extends AbstractDto,E extends BaseEntity,R extends JpaRepository> implements IGenericCrudService<T> {

    @Autowired
    protected R _repository;

    @Autowired
    protected E _entity;

    @Autowired
    protected T _dto;

    @Autowired
    protected ModelMapper modelMapper;

    @Override
    public ModelResult<T> save(T t) throws Exception {
        if(t.getId() > 0 ){
            throw new Exception();
        }
        E entity = (E) modelMapper.map(t, _entity.getClass());
        entity = (E)_repository.save(entity);
        ModelResult<T> result = new  ModelResult.ModelResultBuilder()
                .setData(modelMapper.map(entity,_dto.getClass())).build();
        return result;
    }

    @Override
    public ModelResult<T> update(T t) throws Exception {
        return null;
    }

    @Override
    public ModelResult<Boolean> delete(Long id) throws Exception {
        return null;
    }

    @Override
    public ModelResult<List<T>> getAll() {
        Page<E> mainGroups = _repository.findAll(new PageRequest(0,1000));
        List<T> data= (List<T>) mainGroups.getContent().stream()
                .map(post -> modelMapper.map(post,_dto.getClass()))
                .collect(Collectors.toList());
        return new  ModelResult.ModelResultBuilder<List<T>>()
                .setData(data).build();
    }

    @Override
    public ModelResult<T> findOne(Long id) {
        return null;
    }
}
