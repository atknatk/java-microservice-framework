package com.esys.framework.core.entity.core;

import com.esys.framework.core.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table
@Setter
@Getter
public class PersistentAuditEvent extends BaseEntity{


    @NotNull
    @Column(nullable = false)
    private String principal;


    private LocalDateTime auditEventDate;

    private String auditEventType;

    @ElementCollection
    @MapKeyColumn(name = "name")
    @Column(name = "value")
    @CollectionTable(name = "persistent_audit_evt_data", joinColumns=@JoinColumn(name="id"))
    private Map<String, String> data = new HashMap<>();
}
