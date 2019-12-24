package com.esys.framework.core.entity.core;

import com.esys.framework.core.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class AppConfiguration extends BaseEntity {

    @Column(nullable = false)
    private String key;

    @Column(nullable = false)
    private String value;

    public AppConfiguration(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
