package com.esys.framework.core.configuration;

import com.esys.framework.core.entity.core.AppConfiguration;
import com.esys.framework.core.service.IAppConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class CoreSetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private IAppConfigurationService configurationService;


    /**
     * Core için veritabanına yuklenmesi gereken(varsayilan veriler) configurasyon'dur.
     */
   @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
       if (alreadySetup) {
           return;
       }
       createAppConfiguration("maxFailureLoginAttempt","10");

       alreadySetup = true;
    }

    private final AppConfiguration createAppConfiguration(final String key,final String value) {
        return configurationService.add(key,value);
    }
}
