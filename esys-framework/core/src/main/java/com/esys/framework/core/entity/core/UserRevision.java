package com.esys.framework.core.entity.core;

import com.esys.framework.core.audit.UserRevisionListener;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@RevisionEntity(UserRevisionListener.class)
@Getter
@Setter
public class UserRevision extends DefaultRevisionEntity {

    private static final long serialVersionUID = -7604731515258123883L;

    @Column(name = "rev_user")
    private String user;

}