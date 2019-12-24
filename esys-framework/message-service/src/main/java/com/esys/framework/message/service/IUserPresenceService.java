package com.esys.framework.message.service;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
public interface IUserPresenceService {
    void postSend(Message<?> message, MessageChannel channel, boolean sent);
}
