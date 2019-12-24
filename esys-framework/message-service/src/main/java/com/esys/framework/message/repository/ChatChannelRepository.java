package com.esys.framework.message.repository;

import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.repository.SoftDeleteCrudRepository;
import com.esys.framework.message.entity.ChatChannel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */

@Transactional
@Repository
public interface ChatChannelRepository extends SoftDeleteCrudRepository<ChatChannel, Long> {

    List<ChatChannel> findAllByUsers(User user);

    Optional<ChatChannel> findByChannelName(String channelName);

//    @Query("select c from ChatChannel c join c.users u where u in :users")
//    Optional<ChatChannel> findChatChannel(@Param("users") Collection<User> users);

    @Query("select distinct c.channelName from ChatChannel c join c.users u where u = :user")
    List<String> findAllOwnChatChannel(@Param("user") User user);

    @Query("select c from ChatChannel c join c.users u where u = :user")
    List<ChatChannel> findAllOwnChatChannelObj(@Param("user") User user);

    @Query("select c from ChatChannel c join c.users u where  c.channelName in  :channelNames and u = :user")
    Optional<ChatChannel> findChatChannelByOtherUser(@Param("channelNames") Collection<String> channelNames, @Param("user") User user);

}
