package com.esys.framework.core.service;

import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.model.ModelResult;

import java.util.List;

public interface IGenericCrudService<T extends AbstractDto> {


   // String rolePrefixName();

   // @PreAuthorize("hasPermission(rolePrefixName())")
    ModelResult<T> save(final T t) throws Exception;

    ModelResult<T> update(final T t) throws Exception;

    ModelResult<Boolean> delete(final Long id) throws Exception ;

    ModelResult<List<T>> getAll() ;

    ModelResult<T> findOne(final Long id) throws Exception;
}
