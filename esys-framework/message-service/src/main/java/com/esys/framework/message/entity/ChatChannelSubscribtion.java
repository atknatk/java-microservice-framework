package com.esys.framework.message.entity;

import com.esys.framework.core.entity.AbstractSoftDeleteAuditingEntity;
import com.esys.framework.core.entity.BaseEntity;
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
@Data
public class ChatChannelSubscribtion extends BaseEntity {

    @NotNull
    @OneToOne
    private ChatChannel channelName;

    @NotNull
    @OneToOne
    private User user;

    @NotNull
    private String subscribeId;



}
