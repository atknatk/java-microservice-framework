package tr.com.esys.framework.da.test;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tr.com.esys.framework.da.dao.DocumentRepository;
import tr.com.esys.framework.da.dao.DocumentVersionRepository;
import tr.com.esys.framework.da.service.DAService;
import tr.com.esys.framework.da.service.impl.DefaultDAService;
import tr.com.esys.framework.da.web.DAController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Mustafa Kerim Yılmaz
 * mustafa.yilmaz@isisbilisim.com.tr
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ImportAutoConfiguration({RibbonAutoConfiguration.class, FeignRibbonClientAutoConfiguration.class, FeignAutoConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {
        DAControllerTest.Configuration.class
})
@WebAppConfiguration
public class DAControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private DAController controller;

    @Autowired
    DAService defaultDAService;

    @MockBean
    private DocumentRepository documentRepository;

    @MockBean
    private DocumentVersionRepository documentVersionRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @After
    public void tearDown() {

    }

    @TestConfiguration
    @EnableTransactionManagement
    @ComponentScan({"tr.com.esys.framework", "com.esys.framework"})
    @WebAppConfiguration
    static class Configuration {

        @Bean
        public DAService defaultDAService() {
            return new DefaultDAService();
        }

    }

    @Test
    @WithMockUser(username = "user", password = "pwd", roles = "USER")
    public void testAdd() throws Exception {

        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
        MockMultipartFile secondFile = new MockMultipartFile("data", "other-file-name.data", "text/plain", "some other type".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/documents/upload/1")
                .file(firstFile)
                .file(secondFile)
                .param("some-random", "4"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "pwd", roles = "USER")
    public void testGet() throws Exception {

        mockMvc.perform(get("/documents/1/1"))
                .andExpect(status().isOk());
    }

}
