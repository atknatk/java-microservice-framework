package com.esys.framework.core.entity.organization;

import com.esys.framework.core.entity.AbstractAuditingEntity;
import com.esys.framework.core.entity.uaa.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class Supplier extends AbstractAuditingEntity {


    private String name;
    private boolean active;
    private String type;



    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE},optional = false)
    @JoinColumn(name="customers_id", nullable=false)
    //@OnDelete(action = OnDeleteAction.CASCADE)
    private Customer customer;

    public Supplier(long id) {
        super(id);
    }

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "suppliers_users",
            joinColumns = @JoinColumn(name = "supplier_id", referencedColumnName = "id"),
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
