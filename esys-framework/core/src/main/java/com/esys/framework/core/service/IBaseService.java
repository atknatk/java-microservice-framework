package com.esys.framework.core.service;

import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.entity.BaseEntity;

import java.util.List;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
public interface IBaseService<DTO extends AbstractDto> {

    DTO save(DTO dto);
    DTO update(DTO dto);
    DTO findById(Long id);
    List<DTO> findAll();
    void delete(Long id);
}
