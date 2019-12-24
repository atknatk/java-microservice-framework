package com.esys.framework.uaa.service;

import com.esys.framework.core.dto.uaa.AuthorityDto;
import com.esys.framework.core.dto.uaa.RoleDto;

import java.util.List;

public interface IAuthorityService {

    List<AuthorityDto> findAll();
    List<AuthorityDto> getAuthoritiesByRoleId(long id);
    void request(String name);
}
