package com.esys.framework.message.entity;

import com.esys.framework.core.entity.AbstractAuditingEntity;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.message.enums.MessageType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Getter
@Setter
public class ChatMessage extends AbstractAuditingEntity {

  @OneToOne
  private User ownerUser;

  @NotNull
  private MessageType messageType ;

  @NotNull
  private String contents;

  private LocalDateTime sendTime;

  private LocalDateTime readTime;

  @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE},optional = false)
  @JoinColumn(name="chat_channel_id", nullable=false)
  private ChatChannel chatChannel;

  //@OneToMany(fetch = FetchType.LAZY)
  //private Collection<ChatMessageStatus> chatMessageStatuses;




}
