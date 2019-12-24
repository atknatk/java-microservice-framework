package com.esys.framework.core.entity.uaa;

import com.esys.framework.core.entity.AbstractSoftDeleteAuditingEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.envers.Audited;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table
@Data
@DynamicUpdate
public class AuthorityRequest extends AbstractSoftDeleteAuditingEntity {

    @OneToOne
    private User user;

    @OneToOne
    private Authority authority;

}
