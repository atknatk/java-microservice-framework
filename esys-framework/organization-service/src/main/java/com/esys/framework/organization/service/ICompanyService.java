package com.esys.framework.organization.service;

import com.esys.framework.core.dto.organization.CompanyDto;
import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.core.service.IGenericCrudService;

import java.util.List;

public interface ICompanyService extends IGenericCrudService<CompanyDto> {

    ModelResult<List<CompanyDto>> findCompaniesByGroupId(long id);
    List<BasicUserDto> users(Long id);
    void saveUsers(Long id, List<User> users);

    void delete(Long id, User user);
}
