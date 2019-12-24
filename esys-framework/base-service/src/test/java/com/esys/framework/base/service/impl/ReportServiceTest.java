package com.esys.framework.base.service.impl;

import com.esys.framework.base.enums.ReportName;
import com.esys.framework.base.repository.IReportVersionRepository;
import com.esys.framework.base.service.IReportService;
import com.esys.framework.base.service.ISignService;
import org.junit.FixMethodOrder;
import org.junit.Test;
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
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ReportService.class})
@DataJpaTest
@ContextConfiguration(classes = {ReportServiceTest.Configuration.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ImportAutoConfiguration({RibbonAutoConfiguration.class, FeignRibbonClientAutoConfiguration.class, FeignAutoConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReportServiceTest {


    boolean is = false;
    @Autowired
    private transient IReportService reportService;


    @Test
    public void generate() {
        //reportService.export(new ArrayList(),new String[]{""});
        assertThat(is).isFalse();
    }


    @TestConfiguration
    @EnableTransactionManagement
    @ComponentScan({"com.esys.framework"})
    static class Configuration{

        @Autowired
        private transient MessageSource messageSource;

        @Autowired
        private transient IReportVersionRepository reportVersionRepository;

        @Bean
        public ISignService signService() {
            return new SignService();
        }


        @Bean
        public IReportService reportService() {
            return new ReportService(messageSource,signService(),reportVersionRepository);
        }


    }

}
