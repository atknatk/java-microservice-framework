package com.esys.framework.core.entity.uaa;

import com.esys.framework.core.entity.AbstractSoftDeleteAuditingEntity;
import com.esys.framework.core.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table
@Getter
@Setter
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Audited
public class Role extends AbstractSoftDeleteAuditingEntity {

    private String name;

    @Column(updatable = false)
    private boolean isPredefined = false;

    private boolean isDefault = false;

    @ColumnDefault("false")
    private boolean isBpm = false;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "roles_authorities",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Collection<Authority> authorities;

    public Role() {
        super();
    }

    public Role(Long id) {
        super(id);
    }

    public Role(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object obj) {
        return name.equals(((Role)obj).getName());
    }
}
