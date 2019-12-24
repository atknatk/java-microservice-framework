package com.esys.framework.message.web.controller;

import com.codahale.metrics.annotation.Timed;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.core.security.SecurityUtils;
import com.esys.framework.core.web.controller.BaseController;
import com.esys.framework.message.dto.ChatChannelDto;
import com.esys.framework.message.dto.ChatMessageDto;
import com.esys.framework.message.dto.FriendsDto;
import com.esys.framework.message.service.IChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController extends BaseController {

    @Autowired
    private transient IChatService chatService;

    @Autowired
    private MessageSource messages;


    @MessageMapping("/channel.{channelId}")
    @SendTo("/topic/channel.{channelId}")
    public ChatMessageDto chatMessage(@DestinationVariable String channelId,
                                      /*SimpMessageHeaderAccessor headerAccessor,*/
                                      @Header("Authorization") String jwt,
                                      ChatMessageDto message) throws BeansException {
        SecurityUtils.loginWithJwt(jwt);
        return chatService.submitMessage(channelId,message);
    }

    @MessageMapping("/user.chat.read.{userId}")
    @SendTo("/topic/user.chat.read.{userId}")
    public ChatMessageDto userChat(@DestinationVariable String userId,
                                      @Header("Authorization") String jwt,
                                      ChatMessageDto message) throws BeansException {
        SecurityUtils.loginWithJwt(jwt);
        chatService.readMessage(message);
        return message;
    }

    @Timed
    @GetMapping(  path = "/friends", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModelResult<?>> friends() {
        ModelResult<List<FriendsDto>> list = chatService.retrieveFriendsList();
        return getResponseWithData(list,HttpStatus.OK);
    }

    @Timed
    @PutMapping(  path = "/channel/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModelResult<?>> friends(@Valid @PathVariable("id") long id) {
        ChatChannelDto chatChannel = chatService.establishChatSession(id);
        ModelResult.ModelResultBuilder builder = new ModelResult.ModelResultBuilder(messages,LocaleContextHolder.getLocale());
        return getResponseWithData(builder.setData(chatChannel).build() ,HttpStatus.OK);
    }

    @GetMapping(value="/channel/{channelName}/message", produces={MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModelResult<?>> getExistingChatMessages(@PathVariable("channelName") String channelName) {
        List<ChatMessageDto> chatMessages = chatService.getExistingChatMessages(channelName);
        ModelResult.ModelResultBuilder builder = new ModelResult.ModelResultBuilder(messages, LocaleContextHolder.getLocale());
        return getResponseWithData(builder.setData(chatMessages).build() ,HttpStatus.OK);
    }


}
