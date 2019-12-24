package tr.com.esys.framework.da.test;

import com.esys.framework.core.entity.organization.Company;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.da.entity.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.WebApplicationContext;
import tr.com.esys.framework.da.dao.DocumentRepository;
import tr.com.esys.framework.da.dao.DocumentVersionRepository;
import tr.com.esys.framework.da.service.DAService;
import tr.com.esys.framework.da.service.impl.DefaultDAService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Mustafa Kerim Yılmaz
 * mustafa.yilmaz@isisbilisim.com.tr
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DAService.class})
@DataJpaTest
@ContextConfiguration(classes = {DAServiceTest.Configuration.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ImportAutoConfiguration({RibbonAutoConfiguration.class, FeignRibbonClientAutoConfiguration.class, FeignAutoConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DAServiceTest {

    @MockBean
    private DocumentRepository documentRepository;

    @MockBean
    private DocumentVersionRepository documentVersionRepository;

    @Autowired
    DAService defaultDAService;

    @After
    public void tearDown() {

    }

    @TestConfiguration
    @EnableTransactionManagement
    @ComponentScan({"tr.com.esys.framework", "com.esys.framework"})
    static class Configuration{

        @Bean
        public DAService defaultDAService() {
            return new DefaultDAService();
        }

    }

    @Test
    public void testCalculatePath() throws Exception {
        Path path = defaultDAService.calculatePath(1,1);
        assertThat(path.toString()).isEqualTo("1/1");
    }

    @Test
    public void testAdd() throws Exception {
        final Company company = new Company();
        final User user = new User();
        final InputStream inputStream = new ByteArrayInputStream(new byte[5]);
        Document document = defaultDAService.add(company,user, "test", inputStream);
        assertThat(document).isNull();
    }
}
