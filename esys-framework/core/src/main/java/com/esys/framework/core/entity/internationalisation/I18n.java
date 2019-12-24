package com.esys.framework.core.entity.internationalisation;

import com.esys.framework.core.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
public class I18n extends BaseEntity {

    @Column(unique = true)
    private String code;

    @ColumnDefault("''")
    private String tr = "";

    /**
     * English
     */
    @ColumnDefault("''")
    private String en = "";

    /**
     * Germany
     */
    @ColumnDefault("''")
    private String de = "";

    /**
     * French
     */
    @ColumnDefault("''")
    private String fr = "";

    /**
     * Mandarin Chinese
     */
    @ColumnDefault("''")
    private String zh = "";


    /**
     * Spanish
     */
    @ColumnDefault("''")
    private String es = "";

    /**
     * Arabic
     */
    @ColumnDefault("''")
    private String ar = "";

    /**
     * Russian
     */
    @ColumnDefault("''")
    private String ru = "";

    /**
     * Japanese
     */
    @ColumnDefault("''")
    private String ja = "";

    /**
     * Portuguese
     */
    @ColumnDefault("''")
    private String pt = "";

    /**
     * Italy
     */
    @ColumnDefault("''")
    private String it = "";

    /**
     * Hindi
     */
    @ColumnDefault("''")
    private String hi = "";

    public I18n() {
    }

    public I18n(String code) {
        this.code = code;
    }
}
