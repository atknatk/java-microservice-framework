package com.esys.framework.zuul.security;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Thibaud Leprêtre
 */
@Configuration
@EnableOAuth2Sso
@Order(value = 0)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final String[] PUBLIC_MATCHERS = {
            "/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/swagger-resources/configuration/ui",
            "/swagger-ui.html",
            "/swagger-resources/configuration/security",
            "login",
            "/api/**",
            "/eureka/**",
            /*"api/uaa/oauth/token"*/

            // swagger
            "**/v2/api-docs",
            // registration
            "/account/registrationConfirm*",
            "/user/expiredAccount*",
            "/account/register*",
            "/account/register/captcha*",
            "/uaa/account/register/captcha*",
            "/user/resendRegistrationToken*",
            "/user/forgetPassword*",
            "/user/resetPassword*",
            /*"api/uaa/oauth/token"*/

            "/account/oauth2/client",
            "/oauth_login"

    };


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(PUBLIC_MATCHERS).permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();
    }



}
