package com.esys.framework.base.entity;

import com.esys.framework.base.currency.Moneys;
import com.esys.framework.core.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@Data
@Entity
public class Currency extends BaseEntity {


    private Date date;

    @Enumerated(EnumType.STRING)
    private Moneys name;

    @Column(precision=15, scale=4)
    private float buyingPrice;

    @Column(precision=15, scale=4)
    private float sellingPrice;

    private boolean forex;
}
