package com.esys.framework.message.entity;

import com.esys.framework.core.entity.AbstractAuditingEntity;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.message.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@Entity
@Getter
@Setter
@BatchSize(size = 20)
public class ChatUserStatus extends AbstractAuditingEntity {

    @OneToOne
    private User user;

    private UserStatus userStatus;

    private String connectionId;
}
