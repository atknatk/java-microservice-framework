package com.esys.framework.base.service.impl;

import com.esys.framework.base.currency.Moneys;
import com.esys.framework.base.entity.Currency;
import com.esys.framework.base.service.ICurrencyService;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.thymeleaf.spring5.SpringTemplateEngine;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CurrencyService.class})
@DataJpaTest
@ContextConfiguration(classes = {CurrencyServiceTest.Configuration.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ImportAutoConfiguration({RibbonAutoConfiguration.class, FeignRibbonClientAutoConfiguration.class, FeignAutoConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CurrencyServiceTest {

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @Autowired
    private ICurrencyService currencyService;


    @TestConfiguration
    @EnableTransactionManagement
    @ComponentScan({"com.esys.framework"})
    static class Configuration{
        @Bean
        public ICurrencyService defaultDAService() {
            return new CurrencyService();
        }

        @Bean
        public JavaMailSender mailSender() {
            return new JavaMailSenderImpl();
        }
        @Bean
        public SpringTemplateEngine springTemplateEngine(){
            return new SpringTemplateEngine();
        }
        @Autowired
        private JavaMailSender mailSender;

    }

    @org.junit.Test
    public void getPrice() {
        Currency currency = currencyService.getPrice(Moneys.US_DOLLAR);
        assertThat(currency.getDate()).isNotNull();
        assertThat(currency.getBuyingPrice()).isNotNull();

        // Dolar 5'in altına düşmezz :))
        assertThat(currency.getBuyingPrice()).isGreaterThan(5);
    }
}
