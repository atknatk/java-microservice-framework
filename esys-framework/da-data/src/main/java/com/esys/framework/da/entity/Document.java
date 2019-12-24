package com.esys.framework.da.entity;

import com.esys.framework.core.entity.AbstractAuditingEntity;
import com.esys.framework.core.entity.AbstractSoftDeleteAuditingEntity;
import com.esys.framework.core.entity.organization.Company;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Collection;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

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
public class Document extends AbstractAuditingEntity {

    private String fileName;

    @ManyToOne(optional = false)
    @Audited(targetAuditMode = NOT_AUDITED)
    private Company company;

    @Column(length = 100)
    private String description;

    @ManyToMany
    @JoinTable(name = "document2version",
            joinColumns = @JoinColumn(name = "document_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "version_id", referencedColumnName = "id"))
    private Collection<DocumentVersion> versions;

}
