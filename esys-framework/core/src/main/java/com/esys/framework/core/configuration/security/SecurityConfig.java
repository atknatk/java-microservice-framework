package com.esys.framework.core.configuration.security;

import com.esys.framework.core.configuration.EsysProperties;
import com.esys.framework.core.entity.uaa.IpAuthentication;
import com.esys.framework.core.security.CustomAuthoritiesExtractor;
import com.esys.framework.core.security.CustomPrincipalExtractor;
import com.esys.framework.core.service.impl.IpAuthentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@EnableResourceServer
@Configuration
public class SecurityConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private IpAuthentationService ipAuthentationService;

    @Autowired
    private EsysProperties esysProperties;

    // Token kontrolu istenilmeyen isteklerin pattern'i.

    public static final String[] PUBLIC_MATCHERS = {
            // swagger
            "**/v2/api-docs",
            "/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/swagger-resources/configuration/ui",
            "/swagger-ui.html",
            "/swagger-resources/configuration/security",


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
            "/oauth_login",
            "/i18n/**",
            "/ws/**",
    };


    /**
     * Ip filtreleme ve yetki icin yapılan genel config
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry builder = http.authorizeRequests()
                .antMatchers(PUBLIC_MATCHERS).permitAll();

        List<IpAuthentication> ipAuthenticationList = ipAuthentationService.findAll();
        if(ipAuthenticationList.size() == 0){
            builder.anyRequest().authenticated();
        }else{
            for (IpAuthentication ip : ipAuthenticationList){
                builder.anyRequest().access("isAuthenticated() and hasIpAddress('"+ip.getIp()+"')");
            }
        }

        http.addFilterBefore(new AuthenticationTokenFilter(esysProperties.getSecurity().getAuthentication().getJwt().getSecret()), UsernamePasswordAuthenticationFilter.class);
      // builder.and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }

    @Bean
    public PrincipalExtractor principalExtractor() {
        return new CustomPrincipalExtractor();
    }

    @Bean
    public AuthoritiesExtractor authoritiesExtractor() {
        return new CustomAuthoritiesExtractor();
    }
}
