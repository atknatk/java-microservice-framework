package com.esys.framework.base.configuration;

import com.esys.framework.base.service.ICurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private ICurrencyService currencyService;

    // API

    /**
     * Sistem ayaga kalktigi zaman currency degerlerini alir.
     * @param event event
     */
    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        currencyService.retriveCurrency();
        alreadySetup = true;
    }

}
