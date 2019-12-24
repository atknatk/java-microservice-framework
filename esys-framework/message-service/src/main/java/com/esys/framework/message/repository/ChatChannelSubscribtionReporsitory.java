package com.esys.framework.message.repository;

import com.esys.framework.core.entity.BaseEntity;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.message.entity.ChatChannel;
import com.esys.framework.message.entity.ChatChannelSubscribtion;
import lombok.Data;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@Transactional
@Repository
public interface ChatChannelSubscribtionReporsitory extends CrudRepository<ChatChannelSubscribtion, Long> {

    Optional<ChatChannelSubscribtion> findBySubscribeId(String subscribeId);

    Optional<ChatChannelSubscribtion> findBySubscribeIdAndUser(String subscribeId, User user);

    Optional<ChatChannelSubscribtion> findByChannelNameAndUser(ChatChannel chatChannel, User user);

    List<ChatChannelSubscribtion> findAllByChannelNameAndUser(ChatChannel chatChannel, User user);

    @Modifying
    @Transactional
    void deleteAllByUser(User user);


}
