package com.esys.framework.core.entity.uaa;

import com.esys.framework.core.entity.AbstractSoftDeleteAuditingEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table
@Getter
@Setter
@Audited
@Where(clause="\"deleted\" = false")
public class UserGroup extends AbstractSoftDeleteAuditingEntity {

    @Column(unique=true,nullable = false)
    private String name;

    private boolean enabled = true;

    private boolean isDefault = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_groups_users",
            joinColumns = @JoinColumn(name = "user_group_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Collection<User> users;

    public UserGroup() {
        super();
    }

    public UserGroup(Long id) {
        super(id);
    }

    public UserGroup(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object obj) {
        return name.equals(((UserGroup)obj).getName());
    }
}
