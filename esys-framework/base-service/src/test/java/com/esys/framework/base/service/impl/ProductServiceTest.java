package com.esys.framework.base.service.impl;

import com.esys.framework.base.currency.ICurrency;
import com.esys.framework.base.currency.Moneys;
import com.esys.framework.base.dto.ProductDto;
import com.esys.framework.base.entity.Currency;
import com.esys.framework.base.repository.IProductRepository;
import com.esys.framework.base.repository.IReportVersionRepository;
import com.esys.framework.base.service.ICurrencyService;
import com.esys.framework.base.service.IProductService;
import com.esys.framework.base.service.IReportService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.modelmapper.ModelMapper;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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
public class ProductServiceTest {


    @Autowired
    private transient IProductService productService;

    @Autowired
    private transient ICurrencyService currencyService;


    @Test
    public void getProductByCurrency() {
        ProductDto product = new ProductDto();
        product.setName("getProductByCurrency");
        product.setPrice(10);
        product.setMoney(Moneys.TURKISH_TL);
        product = productService.save(product);

        ProductDto productDto = productService.getProductByCurrency(product.getId(),Moneys.US_DOLLAR);
        Currency currency =  currencyService.getPrice(Moneys.US_DOLLAR);

        assertThat(productDto.getPrice()).isEqualTo(currency.getBuyingPrice() * product.getPrice());

    }


    @Test
    public void save() {

        ProductDto product = new ProductDto();
        product.setName("Test");

        product = productService.save(product);

        assertThat(product).isNotNull();
        assertThat(product.getId()).isGreaterThan(0);

    }

    @TestConfiguration
    @EnableTransactionManagement
    @ComponentScan({"com.esys.framework"})
    static class Configuration{

        @Autowired
        private transient ModelMapper mapper;

        @Autowired
        private transient IProductRepository productRepository;

        @Autowired
        private transient IReportVersionRepository reportVersionRepository;

        @Autowired
        private transient ICurrencyService currencyService;

        @Autowired
        private transient IReportService reportService;

        @Bean
        public ICurrencyService currencyService() {
            return new CurrencyService();
        }

        @Bean
        public IProductService productService() {
            return new ProductService(mapper,productRepository,currencyService,reportService,reportVersionRepository);
        }

    }

}
