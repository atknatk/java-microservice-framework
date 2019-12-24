package com.esys.framework.message.service;

import com.esys.framework.core.model.ModelResult;
import com.esys.framework.message.dto.FriendsDto;
import com.esys.framework.message.repository.ChatChannelRepository;
import com.esys.framework.message.service.impl.ChatService;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ChatService.class})
@DataJpaTest
@ContextConfiguration(classes = {ChatServiceTest.Configuration.class})
@EnableAutoConfiguration
@ImportAutoConfiguration({RibbonAutoConfiguration.class, FeignRibbonClientAutoConfiguration.class, FeignAutoConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ChatServiceTest  {

    @Autowired
    IChatService chatService;


    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    //@WithUserDetails()
    public void retrieveFriendsList() {
        ModelResult<List<FriendsDto>> result  = chatService.retrieveFriendsList();
        Assert.assertTrue(result.isSuccess());
    }

    @org.junit.Test
    public void establishChatSession() {
    }

    @org.junit.Test
    public void getExistingChannel() {
    }

    @org.junit.Test
    public void submitMessage() {
    }

    @org.junit.Test
    public void getExistingChatMessages() {
    }

    @TestConfiguration
    @EnableTransactionManagement
    @ComponentScan({"com.esys.framework"})
    static class Configuration{

        @Bean
        public ModelMapper mapper() {
            return new ModelMapper();
        }

        @Autowired
        private ChatChannelRepository chatChannelRepository;

        @Bean
        public IChatService chatService() {
            return new ChatService(mapper(),chatChannelRepository);
        }

    }
}
