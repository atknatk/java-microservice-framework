package com.esys.framework.core.entity.organization;

import com.esys.framework.core.entity.AbstractAuditingEntity;
import com.esys.framework.core.entity.uaa.User;
import lombok.*;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class Company extends AbstractAuditingEntity {

    private String name;
    private boolean active;

    @OneToMany(mappedBy="company")
    private Set<BranchOffice> branchOffices;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE},optional = false)
    @JoinColumn(name="groups_id", nullable=false)
    //@OnDelete(action = OnDeleteAction.CASCADE)
    private Group group;

    public Company(long id) {
        super(id);
    }

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "companies_users",
            joinColumns = @JoinColumn(name = "company_id", referencedColumnName = "id"),
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
