package com.esys.framework.core.service;

import com.esys.framework.core.aop.logging.NoLogging;
import com.esys.framework.core.entity.core.AppConfiguration;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IAppConfigurationService {

    AppConfiguration add(String key, String value);

    String get(String key);

    List<String> getAll(String key);

    Map<String, Set<AppConfiguration>> getAll();

}
