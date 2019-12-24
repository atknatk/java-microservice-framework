package com.esys.framework.message.entity;

import com.esys.framework.core.entity.AbstractAuditingEntity;
import com.esys.framework.core.entity.AbstractSoftDeleteAuditingEntity;
import com.esys.framework.core.entity.uaa.User;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@Entity
@ToString(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause="deleted = false")
public class ChatChannel extends AbstractSoftDeleteAuditingEntity {

    @NotNull
    @Column(unique = true)
    private String channelName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "chat_channels_users",
            joinColumns = @JoinColumn(name = "chat_channel_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Collection<User> users;


    @OneToMany(fetch = FetchType.LAZY ,mappedBy = "chatChannel")
    private Collection<ChatMessage> chatMessages;


}
