package com.esys.framework.uaa.service;

import com.esys.framework.core.dto.uaa.UserDto;
import com.esys.framework.core.dto.uaa.UserGroupDto;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.entity.uaa.UserGroup;
import com.esys.framework.core.model.ModelResult;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.List;
import java.util.Optional;

public interface IUserGroupService{

    UserGroupDto saveUserGroup(UserGroupDto userGroupDto);

    UserGroup saveUserGroupIfNotExist(UserGroup userGroupDto);

    UserGroupDto saveUserGroupIfNotExist(UserGroupDto userGroupDto);

    void assignUsers(Long userGroupId, List<UserDto> list);

    void deleteUserGroup(Long id);

    Optional<UserGroupDto> findOne(Long id);

    List<UserGroupDto> findAll();

    List<UserGroupDto> findAllByUser(Long userId);

    ModelResult setEnabled(Long id, boolean enabled);

    boolean exist(Long id);

    DataTablesOutput<UserGroupDto> paging(DataTablesInput input);

    List<UserDto> findUserByUserGroup(long longValue);
}
