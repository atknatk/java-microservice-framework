package com.esys.framework.core.service.impl;

import com.esys.framework.core.aop.logging.NoLogging;
import com.esys.framework.core.entity.core.AppConfiguration;
import com.esys.framework.core.repository.IAppConfigurationRepository;
import com.esys.framework.core.service.IAppConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

@Service
public class AppConfigurationService implements IAppConfigurationService {

    @Autowired
    private IAppConfigurationRepository repository;

    @Override
    @NoLogging
    public AppConfiguration add(String key, String value) {
        Optional<AppConfiguration> configuration = repository.getFirstByKeyAndValue(key,value);
        if(configuration.isPresent()){
            return configuration.get();
        }

       return repository.save(new AppConfiguration(key,value));
    }

    @Override
    @NoLogging
    public String get(String key) {
        Optional<AppConfiguration> configuration = repository.getFirstByKey(key);
        if(configuration.isPresent()){
            return configuration.get().getValue();
        }
        return null;

    }

    @Override
    @NoLogging
    public List<String> getAll(String key) {
        List<AppConfiguration> configurations = repository.getAllByKey(key);
        List<String> list = new ArrayList<>();
        configurations.stream().forEach(conf -> list.add(conf.getValue()));
        return list;
    }

    @Override
    @NoLogging
    public Map<String, Set<AppConfiguration>> getAll() {
        List<AppConfiguration> configurations = repository.findAll();
        return configurations.stream().collect(groupingBy(AppConfiguration::getKey, toSet()));

    }
}
