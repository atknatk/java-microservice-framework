package com.esys.framework.message.service;

import com.esys.framework.core.dto.uaa.UserDto;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.message.dto.ChatChannelDto;
import com.esys.framework.message.dto.ChatMessageDto;
import com.esys.framework.message.dto.ChatUserStatusDto;
import com.esys.framework.message.dto.FriendsDto;
import com.esys.framework.message.entity.ChatChannel;
import com.esys.framework.message.entity.ChatUserStatus;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
public interface IChatService {


    List<ChatChannelDto> getExistingChannel();
    ChatMessageDto submitMessage(String channelId,ChatMessageDto chatMessageDto);

    ModelResult<List<FriendsDto>> retrieveFriendsList();

    ChatChannelDto establishChatSession(long userId);

    List<ChatMessageDto> getExistingChatMessages(String channelName);

    void updateMessageRead(ChatChannel channelName);

    void notifyStatus(ChatUserStatusDto userStatus);

    CompletableFuture setOnline(UserDto userDto, String connectionId);

    CompletableFuture setOffline(String connectionId);

    ChatMessageDto readMessage(ChatMessageDto message);

    Optional<ChatChannel> findByName(String channelName);

}
