package com.esys.framework.core.common;

import com.esys.framework.core.properties.ApplicationProperties;
import com.google.common.base.CaseFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;

import java.io.Serializable;

@SuppressWarnings("unused")
@Slf4j
public class TableCustomNamingStrategy extends SpringPhysicalNamingStrategy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private ApplicationProperties properties;

    @Value("${spring.application.name}")
    @Setter
    @Getter
    private String prefix = properties == null ?  "esys" : properties.getName();

    public static final TableCustomNamingStrategy INSTANCE = new TableCustomNamingStrategy();

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        if (name.getText().endsWith("_aud")) {
            return new Identifier(toPhysicalTableName(StringUtils.EMPTY, name.getText()), true);
        }
        return new Identifier(toPhysicalTableName(name.getText()), true);
    }

    public String toPhysicalTableName(String name) {
        String pref = prefix.split("-")[0]+ "_";
        return toPhysicalTableName(pref, name);
    }

    public String toPhysicalTableName(final String prefix, final String name) {
        return prefix + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        return new Identifier(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name.getText()), true);
    }
}
