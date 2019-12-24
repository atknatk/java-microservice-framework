package com.esys.framework.core.entity.uaa;

import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.entity.AbstractSoftDeleteAuditingEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table
@Getter
@Setter
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Audited
@Where(clause="\"deleted\" = false")
public class User extends AbstractSoftDeleteAuditingEntity implements UserDetails{

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date joinDate;

    @Column(length = 60)
    private String password;

    private boolean enabled;

    private boolean confirmEmail = false;

    private String secret;

    private LocalDateTime lastLoginDate;

    private String phone;

    private String domain = "esys";

    @ColumnDefault("0")
    private int loginAttemptTryCount = 0;


    @JsonIgnore
    private LocalDateTime lastPasswordModifiedDate;


  /*  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<UserRole> userRoles = new HashSet<>();

        @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        Set<GrantedAuthority> authorities = new HashSet<>();
        userRoles.forEach(ur -> authorities.add(new Authority(ur.getAuthority().getName())));
        return authorities;
    }


    */

    @ManyToMany(mappedBy = "users" ,fetch = FetchType.LAZY)
    private Collection<UserGroup> userGroups;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Collection<Role> roles;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        Set<GrantedAuthority> authorities = new HashSet<>();
        roles.forEach(role -> authorities .addAll(role.getAuthorities()));
        Set<GrantedAuthority> finalAuthorities = authorities.stream()
                .distinct()
                .collect(Collectors.toCollection(HashSet::new));
        return finalAuthorities;
    }


    public User() {
        super();
        this.secret = Base32.random();
        this.enabled = false;
    }

    public User(Long id) {
        super();
        this.secret = Base32.random();
        this.enabled = false;
        this.setId(id);
    }

    public User(BasicUserDto basicUserDto) {
        super(basicUserDto.getId());
        this.email = basicUserDto.getEmail();
        this.firstName = basicUserDto.getFirstName();
        this.lastName = basicUserDto.getLastName();
        this.domain = basicUserDto.getDomain();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {return enabled;}


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
