package com.esys.framework.uaa.service.impl;

import com.esys.framework.core.dto.uaa.AuthorityDto;
import com.esys.framework.core.dto.uaa.RoleDto;
import com.esys.framework.core.entity.uaa.Authority;
import com.esys.framework.core.entity.uaa.AuthorityRequest;
import com.esys.framework.core.entity.uaa.Role;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.security.SecurityUtils;
import com.esys.framework.uaa.repository.IAuthorityRepository;
import com.esys.framework.uaa.repository.IAuthorityRequestRepository;
import com.esys.framework.uaa.repository.IRoleRepository;
import com.esys.framework.uaa.service.IAuthorityService;
import com.esys.framework.uaa.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@Transactional
public class AuthorityService implements IAuthorityService {

    @Autowired
    private IAuthorityRepository authorityRepository;

    @Autowired
    private transient IAuthorityRequestRepository authorityRequestRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<AuthorityDto> findAll() {
        return  modelMapper
                .map(authorityRepository.findAll(),new TypeToken<List<AuthorityDto>>() {}.getType());
    }

    @Override
    public List<AuthorityDto> getAuthoritiesByRoleId(long id) {
        return  modelMapper
                .map(authorityRepository
                        .getAuthoritiesByRoles(Arrays.asList(new Role(id))),new TypeToken<List<AuthorityDto>>() {}.getType());
    }

    @Override
    public void request(String roleName) {
        Authority authority = authorityRepository.findByName(roleName);
        if(authority == null){
            return;
        }

        AuthorityRequest authorityRequest = new AuthorityRequest();
        authorityRequest.setAuthority(authority);
        authorityRequest.setUser(new User(SecurityUtils.getCurrentBasicUser()));
        authorityRequestRepository.save(authorityRequest);
    }

}
