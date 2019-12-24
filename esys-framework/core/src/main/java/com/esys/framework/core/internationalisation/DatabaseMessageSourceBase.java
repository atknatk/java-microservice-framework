package com.esys.framework.core.internationalisation;

import com.esys.framework.core.entity.internationalisation.I18n;
import com.esys.framework.core.repository.I18nRepository;
import com.esys.framework.core.service.impl.I18nService;
import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;

@Slf4j
public abstract class DatabaseMessageSourceBase extends AbstractMessageSource {


    @Getter
    @Setter
    private I18nRepository i18nRepository;

    private Messages messages;

    @Inject
    private JdbcTemplate jdbcTemplate;

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        String msg = messages.getMessage(code, locale);
        return createMessageFormat(msg, locale);

    }


    @PostConstruct
    public void init() {

        String i18nQuery = this.getI18NSqlQuery();

        log.info("Initializing message source using query [{}]", i18nQuery);

        this.messages = jdbcTemplate.query(i18nQuery,
                new ResultSetExtractor<Messages>() {
                    @Override
                    public Messages extractData(ResultSet rs)
                            throws SQLException, DataAccessException {

                        return extractI18NData(rs);
                    }
                });
    }




    protected abstract String getI18NSqlQuery();


    protected abstract Messages extractI18NData(ResultSet rs)
            throws SQLException, DataAccessException;

    protected static final class Messages {


        private final I18nRepository repository;

        public Messages(I18nRepository repository){
                this.repository = repository;
        }


        protected List<Locale> acceptedLocales = new ArrayList<>();

        /* <code, <locale, message>> */
        private Map<String, Map<String, String>> messages =new HashMap<>();;

        /**
         * Gelen kayitdi Dil Map'ine ekler
         */
        public void addMessage(String code, Locale locale, String msg) {
            Map<String, String> data = messages.get(code);
            if (data == null) {
                data = new HashMap<String, String>();
                messages.put(code, data);
            }

            data.put(locale.toString(), msg);
        }

        /**
         * Dil dosyasini Map'te yuklu olan veriden ceker ver karsiligini doner
         */
        public String getMessage(String code, Locale locale) {
            if(Strings.isNullOrEmpty(code)){
                return "";
            }
            code = code.replace(" ", ".");
            Map<String, String> data = messages.get(code);
            if(locale == null){
                return  "";
            }
            if(data != null){
                String localeKey = locale.toString().split("_")[0];
                String message =  data.get(localeKey);
                return message == null ? "" : message;
            }
            for (Locale _loc: acceptedLocales) {
                this.addMessage(code,_loc,"");
            }

            repository.save(new I18n(code));
            return "";
            //return data != null ? data.get(locale) : null;
        }
    }

}
