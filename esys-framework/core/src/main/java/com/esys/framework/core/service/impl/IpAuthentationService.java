package com.esys.framework.core.service.impl;

import com.esys.framework.core.aop.logging.NoLogging;
import com.esys.framework.core.entity.uaa.IpAuthentication;
import com.esys.framework.core.repository.IIpAuthenticationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@Transactional
public class IpAuthentationService {

    @Autowired
    private IIpAuthenticationRepository repository;

    @NoLogging
    public List<IpAuthentication> findAll(){
        return repository.findAll();
    }

}
