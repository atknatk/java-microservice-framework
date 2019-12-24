package com.esys.framework.core.entity.organization;

import com.esys.framework.core.entity.AbstractAuditingEntity;
import com.esys.framework.core.entity.uaa.User;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@Audited
public class MainGroup extends AbstractAuditingEntity {

    private String name;
    private boolean active;

    @OneToMany(mappedBy="mainGroup")
    @NotAudited
    private Set<Group> groups;

    public MainGroup(String name) {
        this.name = name;
    }

    public MainGroup(Long id) {
        super(id);
    }


    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "main_groups_users",
            joinColumns = @JoinColumn(name = "main_group_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    @NotAudited
    private Collection<User> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
