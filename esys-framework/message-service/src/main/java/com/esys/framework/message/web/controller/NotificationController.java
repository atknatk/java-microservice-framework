package com.esys.framework.message.web.controller;

import com.esys.framework.core.security.SecurityUtils;
import com.esys.framework.message.dto.ChatMessageDto;
import org.springframework.beans.BeansException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @SendTo("/topic/user.notification.{userId}")
    public String notifications(@DestinationVariable long userId, String message) {
        return message;
    }

    @SendTo("/topic/user.chat.notification")
    public String notifications(String message) {
        return message;
    }
}
