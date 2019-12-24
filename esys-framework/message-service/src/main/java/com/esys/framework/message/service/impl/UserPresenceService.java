package com.esys.framework.message.service.impl;

import com.esys.framework.core.aop.logging.NoLogging;
import com.esys.framework.core.client.UserClient;
import com.esys.framework.core.dto.uaa.UserDto;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.core.security.SecurityUtils;
import com.esys.framework.message.entity.ChatChannel;
import com.esys.framework.message.entity.ChatChannelSubscribtion;
import com.esys.framework.message.repository.ChatChannelSubscribtionReporsitory;
import com.esys.framework.message.service.IChatService;
import com.esys.framework.message.service.IUserPresenceService;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@Slf4j
@Service
public class UserPresenceService extends ChannelInterceptorAdapter implements IUserPresenceService {


    @Autowired
    private transient UserClient userClient;

    @Autowired
    private transient ChatChannelSubscribtionReporsitory reporsitory;


    @Autowired
    @Lazy
    private transient IChatService chatService;

    @NoLogging
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        return message;
    }

    @NoLogging
    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor stompDetails = StompHeaderAccessor.wrap(message);

        if (stompDetails.getCommand() == null) {
            return;
        }

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            NativeMessageHeaderAccessor accessor2 = MessageHeaderAccessor.getAccessor(message, NativeMessageHeaderAccessor.class);
            String token = accessor2.getFirstNativeHeader("Authorization");

            try {
                if (token != null && token.length() > 10) {
             //       SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken());

                    String altToken = token.substring(7);
                    SecurityUtils.loginWithJwt(altToken);
                    ModelResult<UserDto> res = userClient.session(token);
                    chatService.setOnline(res.getData(), accessor.getSessionId());
                }
            } catch (Exception ex) {
                log.error("Chat User get error ", ex);
            }

            //Authentication user = //... ; // access authentication header(s)
            // accessor.setUser(user);
        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            NativeMessageHeaderAccessor accessor2 = MessageHeaderAccessor.getAccessor(message, NativeMessageHeaderAccessor.class);
            String token = accessor2.getFirstNativeHeader("Authorization");
            try {
                if (token != null && token.length() > 10) {
                    String altToken = token.substring(7);
                    SecurityUtils.loginWithJwt(altToken);
                    chatService.setOffline(accessor.getSessionId());
                }
            } catch (Exception ex) {
                log.error("Chat User get error ", ex);
            }
        } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            NativeMessageHeaderAccessor accessor2 = MessageHeaderAccessor.getAccessor(message, NativeMessageHeaderAccessor.class);
            String token = accessor2.getFirstNativeHeader("Authorization");
            try {
                if (token != null && token.length() > 10) {
                    ChatChannelSubscribtion subscription = new ChatChannelSubscribtion();
                    String altToken = token.substring(7);
                    SecurityUtils.loginWithJwt(altToken);
                    ModelResult<UserDto> res = userClient.session(token);
                    subscription.setUser(new User(res.getData()));
                    subscription.setSubscribeId(accessor.getSessionId());
                    String dest = accessor.getDestination();
                    if(dest.contains("/topic/channel.")){
                       String channelStr =   dest.substring("/topic/channel.".length());
                       if(channelStr.length() ==36){
                           final Optional<ChatChannel> byName = chatService.findByName(channelStr);
                           if(byName.isPresent()){
                               subscription.setChannelName(byName.get());
                               final List<ChatChannelSubscribtion> list = reporsitory.findAllByChannelNameAndUser(byName.get(), new User(res.getData()));
                                if(!list.isEmpty()) reporsitory.deleteAll(list);
                               reporsitory.save(subscription);
                           }
                       }
                    }
                }
            } catch (Exception ex) {
                log.error("Chat User get error ", ex);
            }
        }else if (StompCommand.UNSUBSCRIBE.equals(accessor.getCommand())) {
            NativeMessageHeaderAccessor accessor2 = MessageHeaderAccessor.getAccessor(message, NativeMessageHeaderAccessor.class);
            String token = accessor2.getFirstNativeHeader("Authorization");
            String channelName = accessor2.getFirstNativeHeader("channelName");

            try {
                if (token != null && token.length() > 10 && !Strings.isNullOrEmpty(channelName)) {
                    String altToken = token.substring(7);
                    SecurityUtils.loginWithJwt(altToken);
                    ModelResult<UserDto> res = userClient.session(token);
                    ChatChannel chatChannel = getChannelName(channelName);
                    final Optional<ChatChannelSubscribtion> bySubscribeIdAndUser = reporsitory.findByChannelNameAndUser(chatChannel, new User(res.getData()));
                    if(bySubscribeIdAndUser.isPresent()){
                        reporsitory.delete(bySubscribeIdAndUser.get());
                    }
                }
            } catch (Exception ex) {
                log.error("Chat User get error ", ex);
            }
        }
    }


    private ChatChannel getChannelName(String dest){
        if(dest.contains("/topic/channel.")){
            String channelStr =   dest.substring(dest.indexOf("/topic/channel."));
            if(channelStr.length() ==36){
                final Optional<ChatChannel> byName = chatService.findByName(channelStr);
                if(byName.isPresent()){
                    return byName.get();
                }
            }
        }else{
            final Optional<ChatChannel> byName = chatService.findByName(dest);
            if(byName.isPresent()){
                return byName.get();
            }
        }
        throw new IllegalStateException();
    }





}
