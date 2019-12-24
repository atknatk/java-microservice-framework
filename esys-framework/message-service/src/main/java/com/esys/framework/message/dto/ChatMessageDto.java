package com.esys.framework.message.dto;

import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.message.entity.ChatChannel;
import com.esys.framework.message.enums.MessageType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
public class ChatMessageDto extends AbstractDto {

  private BasicUserDto ownerUser;
  private MessageType messageType ;
  private String contents;
  private String clientId;
  private LocalDateTime sendTime;
  private LocalDateTime readTime;





}
