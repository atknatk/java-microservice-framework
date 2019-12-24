package com.esys.framework.core.entity.organization;

import com.esys.framework.core.entity.AbstractAuditingEntity;
import com.esys.framework.core.entity.uaa.User;
import lombok.*;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@ToString(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Group extends AbstractAuditingEntity {

    /*
        User adı
     */
    private String name;
    private boolean active;

    @OneToMany(mappedBy="group")
    private Set<Company> companies;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE},optional = false)
    @JoinColumn(name="main_groups_id", nullable=false)
    // @OnDelete(action = OnDeleteAction.CASCADE)
    private MainGroup mainGroup;

    public Group(Long id) {
        super(id);
    }

    public Group(String name) {
        this.name = name;
    }

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "groups_users",
            joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"),
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
