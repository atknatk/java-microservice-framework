package com.esys.framework.message.repository;

import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.message.dto.FriendsDto;
import com.esys.framework.message.dto.MessageCountDto;
import com.esys.framework.message.entity.ChatChannel;
import com.esys.framework.message.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {

 @Query("select m FROM ChatMessage m join m.chatChannel c WHERE c.channelName = :channelName ORDER BY m.createdDate DESC")
 List<ChatMessage> getExistingChatMessages(@Param("channelName") String channelName, Pageable pageable);

 @Transactional
 @Modifying
 @Query("update ChatMessage cm SET cm.readTime = CURRENT_TIMESTAMP WHERE cm.chatChannel = :chatChannel and cm.readTime is null and cm.ownerUser <> :user")
 void updateMessageRead(@Param("chatChannel") ChatChannel chatChannel, @Param("user") User user);

 @Query("Select new com.esys.framework.message.dto.MessageCountDto(cc.id,count(cm), cc.channelName) " +
         "FROM ChatChannel cc join  cc.chatMessages cm  WHERE cc IN :channels and cm.readTime is null " +
         "and cm.ownerUser <> :owner group by cc.id,cc.channelName")
 List<MessageCountDto> getUnReadMessages(@Param("channels") Collection<ChatChannel> channels, @Param("owner") User owner);


}
