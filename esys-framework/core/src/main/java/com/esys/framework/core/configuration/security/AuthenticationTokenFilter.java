package com.esys.framework.core.configuration.security;

import com.esys.framework.core.security.SecurityUtils;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.WebUtils;
import reactor.ipc.netty.http.server.HttpServerRequest;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
public class AuthenticationTokenFilter implements Filter {

    @Value("${esys.security.authentication.jwt.secret}")
    private String secret;

    public AuthenticationTokenFilter(String secret) {
        this.secret= secret;
    }

    @Override
    public void init(FilterConfig fc) throws ServletException {
    }

    /**
     * Header yering Query param olarak token gonderilmek istenildigi zaman bu konfigurasyon ile istek token kontrolu yapılır.
     * Token ile authentication yapılır.
     * Locale konfigurasyon icin  locale param'i ile eklenir.
     * Query params locale, auth_token
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() != null && context.getAuthentication().isAuthenticated()) {
            // do nothing
        } else {
            Map<String,String[]> params = req.getParameterMap();
            if (!params.isEmpty() && params.containsKey("auth_token")) {
                String token = params.get("auth_token")[0];
                if (token != null) {
                    SecurityUtils.loginWithJwt(token,secret);
                }
            }

            if (!params.isEmpty() && params.containsKey("locale")) {
                String locale = params.get("locale")[0];
                if (!Strings.isNullOrEmpty(locale)) {
                    Locale localeObj = new Locale(locale);
                    res.setLocale(localeObj);
                    HttpServletRequest request = (HttpServletRequest) req;
                    WebUtils.setSessionAttribute(request,SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,localeObj);
                    LocaleContextHolder.setLocale(localeObj);
                }
            }
        }
        fc.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }

}
