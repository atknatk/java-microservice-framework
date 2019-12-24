package com.esys.framework.base.entity;

import com.esys.framework.base.currency.Moneys;
import com.esys.framework.core.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@Data
@Entity
public class Product extends BaseEntity {

    private String name;

    @Enumerated(EnumType.STRING)
    private Moneys money;

    @Column(precision=20, scale=2)
    private float price;

    private float piece;

}
