package com.esys.framework.core.internationalisation;

import com.esys.framework.core.common.TableCustomNamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;

import javax.inject.Named;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Locale;

@Slf4j
@Named("messageSource")
public class DatabaseMessageSource extends DatabaseMessageSourceBase {

    private static final String I18N_TABLE = "i18n";
    private static final String I18N_QUERY = "SELECT * FROM \"%s\"";

    private TableCustomNamingStrategy tableCustomNamingStrategy = new TableCustomNamingStrategy();

    @Override
    protected String getI18NSqlQuery() {
        return String.format(I18N_QUERY, tableCustomNamingStrategy.toPhysicalTableName(I18N_TABLE));
    }

    // Database'de (i18n) ki kolonlari alarak dil olarak sisteme ekler.
    @Override
    protected Messages extractI18NData(ResultSet rs) throws SQLException,
            DataAccessException {
        Messages messages = new Messages(this.getI18nRepository());
        ResultSetMetaData metaData = rs.getMetaData();
        int noOfColumns = metaData.getColumnCount();

        log.info("i18n tablosunun kolonları alınıyor");
        for (int i = 1; i <= noOfColumns; i++) {
            String columnName = metaData.getColumnName(i);
            if (!"code".equalsIgnoreCase(columnName) &&
                    !"id".equalsIgnoreCase(columnName)) {
                log.info("Dil eklendi : {}",columnName);
                messages.acceptedLocales.add(new Locale(columnName));
            }
        }

        while (rs.next()) {
            String key = rs.getString("code");
            for (Locale locale : messages.acceptedLocales) {
                String msg = rs.getString(locale.getLanguage());
                log.debug("Dil eklendi {} {} {}",key,locale.getLanguage(),msg);
                messages.addMessage(key, locale, msg);
            }
        }
        return messages;
    }
}
