package com.esys.framework.message.web.socket;

import com.esys.framework.message.service.impl.UserPresenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.support.converter.PassThruMessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.ReactorNettyTcpStompClient;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Profile("!test")
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
  private final int OUTBOUND_CHANNEL_CORE_POOL_SIZE = 8;

  @Value( "${spring.rabbitmq.host}" )
  private String rabbitmqHost;

    @Value( "${spring.rabbitmq.username}" )
    private String rabbitmqUsername;

    @Value( "${spring.rabbitmq.password}" )
    private String rabbitmqPassword;


  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableStompBrokerRelay("/topic/", "/queue/")
            .setRelayHost(rabbitmqHost)
            .setRelayPort(61613)
            .setClientLogin(rabbitmqUsername)
            .setClientPasscode(rabbitmqPassword);

    config.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
  }

  @Bean
  public UserPresenceService presenceChannelInterceptor() {
    return new UserPresenceService();
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.setInterceptors(presenceChannelInterceptor());
  }

  @Override
  public void configureClientOutboundChannel(ChannelRegistration registration) {
    registration.taskExecutor().corePoolSize(OUTBOUND_CHANNEL_CORE_POOL_SIZE);
    registration.setInterceptors(presenceChannelInterceptor());
  }


  protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
    messages.simpDestMatchers("/*").authenticated();
  }


}
