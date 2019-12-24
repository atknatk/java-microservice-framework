package com.esys.framework.core.configuration;

import com.esys.framework.core.internationalisation.DatabaseMessageSource;
import com.esys.framework.core.repository.I18nRepository;
import com.esys.framework.core.service.impl.I18nService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DatabaseMessageSourceConfiguration {

    @Autowired
    I18nRepository repository;

    /**
     * Dil dosyasi veritabanindan alinmasi icin yapilan config
     */
    @Bean
    @Primary
    public MessageSource messageSource() {
        DatabaseMessageSource messageSource =  new DatabaseMessageSource();
        messageSource.setI18nRepository(repository);
        return messageSource;
    }

}
