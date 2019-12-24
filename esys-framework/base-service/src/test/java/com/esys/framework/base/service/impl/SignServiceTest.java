package com.esys.framework.base.service.impl;

import com.esys.framework.base.consts.SignKey;
import com.esys.framework.base.service.ISignService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.PrivateKey;
import java.security.PublicKey;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)
 * @project esys-framework
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SignService.class})
@ContextConfiguration(classes = {SignServiceTest.Configuration.class})
public class SignServiceTest {

    @Autowired
    private ISignService signService;

    @Test
    public void sign() {
        try {
            final byte[] tests = signService.sign("Test", SignKey.PrivateKey);
            assertThat(tests).isNotEmpty();
        } catch (Exception e) {

        }
    }

    @Test
    public void getPrivate() throws Exception {
        final PrivateKey aPrivate = signService.getPrivate(SignKey.PrivateKey);
        assertThat(aPrivate).isNotNull();
    }

    @Test
    public void verifySignature() throws Exception {
        final byte[] tests = signService.sign("Test", SignKey.PrivateKey);
        final boolean b = signService.verifySignature("Test".getBytes(), tests, SignKey.PublicKey);
        assertThat(b).isTrue();
    }

    @Test
    public void getPublic() throws Exception {

        final PublicKey aPublic = signService.getPublic(SignKey.PublicKey);
        assertThat(aPublic).isNotNull();
    }

    @TestConfiguration
    @ComponentScan({"com.esys.framework"})
    static class Configuration{

        @Bean
        public ISignService signService(){
            return new SignService();
        }


    }
}
