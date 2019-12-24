package com.esys.framework.da.entity;

import com.esys.framework.core.entity.AbstractAuditingEntity;
import com.esys.framework.core.entity.AbstractSoftDeleteAuditingEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Mustafa Kerim Yılmaz
 * mustafa.yilmaz@isisbilisim.com.tr
 */
@Entity
@Table
@Getter
@Setter
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Audited
public class DocumentVersion extends AbstractAuditingEntity {

    private long size;

    private byte[] hash;

    @Column(length = 100)
    private String description;

}
