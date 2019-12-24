package com.esys.framework.organization.service;

import com.esys.framework.core.dto.organization.MainGroupDto;
import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.dto.uaa.UserDto;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.service.IGenericCrudService;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface IMainGroupService extends IGenericCrudService<MainGroupDto> {

    List<BasicUserDto> users(Long id);

    void saveUsers(Long id, List<User> users);

    void delete(Long id, User user);
}
