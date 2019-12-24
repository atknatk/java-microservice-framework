package com.esys.framework.message.converter;

import com.esys.framework.core.converter.TypeMapConfigurer;
import com.esys.framework.message.dto.ChatChannelDto;
import com.esys.framework.message.dto.ChatMessageDto;
import com.esys.framework.message.entity.ChatChannel;
import com.esys.framework.message.entity.ChatMessage;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatMessageConfiguration extends TypeMapConfigurer<ChatMessage, ChatMessageDto> {

    @Override
    public void toDto(TypeMap<ChatMessage, ChatMessageDto> typeMap) {
        //typeMap.addMapping(src -> src.getChatChannel(), ChatMessageDto::setChatChannel);
        //typeMap.addMapping(src -> src.getChatMessageStatuses(), ChatChannelDto::);
        typeMap.addMapping(src -> src.getOwnerUser(), ChatMessageDto::setOwnerUser);

    }

    @Override
    public void toEntity(TypeMap<ChatMessageDto, ChatMessage> typeMap) {

    }
}
