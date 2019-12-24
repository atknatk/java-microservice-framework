package com.esys.framework.message.converter;

import com.esys.framework.core.converter.TypeMapConfigurer;
import com.esys.framework.core.dto.uaa.UserDto;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.message.dto.ChatChannelDto;
import com.esys.framework.message.entity.ChatChannel;
import org.modelmapper.TypeMap;
import org.modelmapper.TypeToken;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Type;
import java.util.List;

@Configuration
public class ChatChannelConfiguration extends TypeMapConfigurer<ChatChannel, ChatChannelDto> {

    @Override
    public void toDto(TypeMap<ChatChannel, ChatChannelDto> typeMap) {
        typeMap.addMapping(src -> src.getUsers(), ChatChannelDto::setUsers);
//        typeMap.addMapping(src -> src.getChatMessages(), ChatChannelDto::setChatMessages);
    }

    @Override
    public void toEntity(TypeMap<ChatChannelDto, ChatChannel> typeMap) {

    }
}
