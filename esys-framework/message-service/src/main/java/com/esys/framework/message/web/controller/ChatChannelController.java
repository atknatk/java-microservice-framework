package com.esys.framework.message.web.controller;

import com.esys.framework.message.dto.ChatMessageDto;
import com.esys.framework.message.service.IChatService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */

@RestController
public class ChatChannelController {

    @Autowired
    private IChatService chatService;


    @SendTo("/topic/user.notification.{userId}")
    public String notifications(@DestinationVariable long userId, String message) {
        return message;
    }


    @MessageMapping("/private.chat.{channelId}")
    @SendTo("/topic/private.chat.{channelId}")
    public ChatMessageDto chatMessage(@DestinationVariable String channelId, ChatMessageDto message) throws BeansException{
        chatService.submitMessage(channelId,message);
        return message;
    }


}
