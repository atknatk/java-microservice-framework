package com.esys.framework.core.entity.uaa;

import com.esys.framework.core.consts.IpType;
import com.esys.framework.core.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class IpAuthentication extends BaseEntity {

    @NotNull
    private String ip;

    @Enumerated(EnumType.STRING)
    private IpType ipType = IpType.WHITELIST;

}
