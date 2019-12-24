package com.esys.framework.uaa.service;

import com.esys.framework.core.dto.uaa.RoleDto;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface IRoleService {

    List<RoleDto> findAll();

    RoleDto saveRole(RoleDto roleDto);

    RoleDto updateRole(RoleDto roleDto);

    boolean deleteRole(long id);

    DataTablesOutput<RoleDto> rolesPaging(@Valid @RequestBody DataTablesInput input);

    RoleDto getByName(String roleName);

    RoleDto getById(long id);

}
