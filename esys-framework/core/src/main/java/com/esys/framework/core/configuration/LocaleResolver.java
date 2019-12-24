package com.esys.framework.core.configuration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Configuration
public class LocaleResolver extends AcceptHeaderLocaleResolver {

    List<Locale> LOCALES = Arrays.asList(
            new Locale("ar"),
            new Locale("de"),
            new Locale("en"),
            new Locale("fr"),
            new Locale("hi"),
            new Locale("it"),
            new Locale("ja"),
            new Locale("pt"),
            new Locale("ru"),
            new Locale("se"),
            new Locale("tr"),
            new Locale("zh")
    );

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        if (StringUtils.isBlank(request.getHeader("Accept-Language"))) {
            return Locale.getDefault();
        }
        List<Locale.LanguageRange> list = Locale.LanguageRange.parse(request.getHeader("Accept-Language"));
        Locale locale = Locale.lookup(list, LOCALES);
        return locale ;
    }
}
