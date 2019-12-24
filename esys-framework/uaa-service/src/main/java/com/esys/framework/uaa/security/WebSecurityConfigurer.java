package com.esys.framework.uaa.security;

import com.esys.framework.core.configuration.EsysProperties;
import com.esys.framework.uaa.service.IUserGroupService;
import com.esys.framework.uaa.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.NimbusAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// @EnableOAuth2Sso
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private static List<String> clients = Arrays.asList("google","facebook");
    private static String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";


    @Autowired
    private Environment env;


    @Autowired
    private IUserService userService;

    @Autowired
    private IUserGroupService userGroupService;

    @Autowired
    private EsysProperties esysProperties;

    private BCryptPasswordEncoder passwordEncoder(){
        return  SecurityUtility.passwordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        return new NimbusAuthorizationCodeTokenResponseClient();
    }


    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = clients.stream()
                .map(c -> getRegistration(c))
                .filter(registration -> registration != null)
                .collect(Collectors.toList());

        return new InMemoryClientRegistrationRepository(registrations);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf()
                .disable();
        http
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http
                .formLogin()
                .loginPage("/login").permitAll().and().authorizeRequests()
                .anyRequest().authenticated();
        http
                .authorizeRequests()
                .antMatchers("/oauth_login", "/loginFailure", "/")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .loginPage("/oauth_login")
                .authorizationEndpoint()
                .baseUri("oauth2/authorize-client")
                .authorizationRequestRepository(authorizationRequestRepository())
                .and()
                .tokenEndpoint()
                .accessTokenResponseClient(accessTokenResponseClient());

    }


    /**
     * Ldap config
     * @param auth AuthenticationManagerBuilder
     * @throws Exception throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        EsysProperties.Security.Authentication.Ldap ldap = esysProperties.getSecurity().getAuthentication().getLdap();
//   curl -X POST http://clientapp:password@localhost:8050/uaa/oauth/token -H 'Content-Type: application/x-www-form-urlencoded' -H 'cache-control: no-cache' -d 'scope=mobileclient&grant_type=password&username=euler&password=password&domain=isis'
        auth.authenticationProvider(authProvider());
        if(ldap.isEnabled()){
            auth
                    .ldapAuthentication()
                    .contextSource()
                    .url(ldap.getUrl() + ldap.getBaseDn())
                    .managerDn(ldap.getUsername())
                    .managerPassword(ldap.getPassword())
                    .and()
                    .userDnPatterns(ldap.getUserDnPattern());
        }
    }


    public AuthenticationFilter authenticationFilter() throws Exception {
        AuthenticationFilter filter = new AuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }


    public AuthenticationProvider authProvider() {
        UserDetailsAuthenticationProvider provider
                = new UserDetailsAuthenticationProvider(passwordEncoder(), userService,userGroupService);
        return provider;
    }


    private ClientRegistration getRegistration(String client) {
        String clientId = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-id");

        if (clientId == null) {
            return null;
        }

        String clientSecret = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-secret");
        if (client.equals("google")) {
            return CommonOAuth2Provider.GOOGLE.getBuilder(client)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .build();
        }
        if (client.equals("facebook")) {
            return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .build();
        }
        return null;
    }

}


/*

import com.esys.framework.core.configuration.EsysProperties;
import com.esys.framework.core.configuration.security.SecurityConfig;
import com.esys.framework.uaa.service.impl.UserService;
import net.logstash.logback.encoder.org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.endpoint.NimbusAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.esys.framework.core.configuration.security.SecurityConfig.PUBLIC_MATCHERS;

@EnableOAuth2Client
//@EnableOAuth2Sso
@Configuration
@Order(-1)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {


    private static List<String> clients = Arrays.asList("google","facebook");
    private static String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";

    private static final String[] PUBLIC_MATCHERS_UAA = {
            "/",
            "/login**",
            "/webjars/**",
            "/error**"
    };


    @Autowired
    private Environment env;


    @Autowired
    private UserService userService;

    @Autowired
    private EsysProperties esysProperties;

    private BCryptPasswordEncoder passwordEncoder(){
        return  SecurityUtility.passwordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        return new NimbusAuthorizationCodeTokenResponseClient();
    }

    @Bean
    protected RestTemplate restTemplate() {
        return new OAuth2RestTemplate(oAuthDetails());
    }


    @Bean
    @ConfigurationProperties("example.oauth2.client")
    protected ClientCredentialsResourceDetails oAuthDetails() {
        return new ClientCredentialsResourceDetails();
    }


    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = clients.stream()
                .map(c -> getRegistration(c))
                .filter(registration -> registration != null)
                .collect(Collectors.toList());

        return new InMemoryClientRegistrationRepository(registrations);
    }


//    @Bean
//    public OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext oauth2ClientContext,
//                                                 OAuth2ProtectedResourceDetails details) {
//        return new OAuth2RestTemplate(details, oauth2ClientContext);
//    }



    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf()
                .disable();
        http
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http
                .formLogin()
                .loginPage("/login").permitAll().and().authorizeRequests()
                .anyRequest().authenticated();
        http
                .authorizeRequests()
                .antMatchers("/oauth_login","/account/oauth2/client", "/loginFailure", "/")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize-client")
                .authorizationRequestRepository(authorizationRequestRepository())
                .and()
                .tokenEndpoint()
                .accessTokenResponseClient(accessTokenResponseClient())
                .and()
                .defaultSuccessUrl("/loginSuccess")
                .failureUrl("/loginFailure")


//        String[] both = (String[]) ArrayUtils.addAll(PUBLIC_MATCHERS, PUBLIC_MATCHERS_UAA);
//
//
//        http.antMatcher("/**").authorizeRequests().antMatchers(both).permitAll().anyRequest()
//                .authenticated().and().exceptionHandling()
//                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/")).and().logout()
//                .logoutSuccessUrl("/").permitAll().and().csrf()
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
//                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
    ;
    }


    @Bean
    @ConfigurationProperties("google")
    public ClientResources google() {
        return new ClientResources();
    }

    private Filter ssoFilter() {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();
        filters.add(ssoFilter(google(), "/login/google"));
        filter.setFilters(filters);
        return filter;
    }


    @Autowired
    OAuth2ClientContext oauth2ClientContext;

    private Filter ssoFilter(ClientResources client, String path) {
        OAuth2ClientAuthenticationProcessingFilter oAuth2ClientAuthenticationFilter = new OAuth2ClientAuthenticationProcessingFilter(path);
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
        oAuth2ClientAuthenticationFilter.setRestTemplate(oAuth2RestTemplate);
        UserInfoTokenServices tokenServices = new UserInfoTokenServices(client.getResource().getUserInfoUri(),
                client.getClient().getClientId());
        tokenServices.setRestTemplate(oAuth2RestTemplate);
        oAuth2ClientAuthenticationFilter.setTokenServices(tokenServices);
        return oAuth2ClientAuthenticationFilter;
    }


    */
/**
     * curl -X POST \
     * http://clientapp:password@localhost:8050/uaa/oauth/token \
     * -H 'Content-Type: application/x-www-form-urlencoded' \
     * -H 'cache-control: no-cache' \
     * -d 'scope=mobileclient&grant_type=password&username=euler&password=password&domain=isis'
     *
     * @param auth
     * @throws Exception
     *//*


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        EsysProperties.Security.Authentication.Ldap ldap = esysProperties.getSecurity().getAuthentication().getLdap();

        auth.authenticationProvider(authProvider());
        if(ldap.isEnabled()){
            auth
                    .ldapAuthentication()
                    .contextSource()
                    .url(ldap.getUrl() + ldap.getBaseDn())
                    .managerDn(ldap.getUsername())
                    .managerPassword(ldap.getPassword())
                    .and()
                    .userDnPatterns(ldap.getUserDnPattern());
        }
    }


    public AuthenticationFilter authenticationFilter() throws Exception {
        AuthenticationFilter filter = new AuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }


    public AuthenticationProvider authProvider() {
        UserDetailsAuthenticationProvider provider
                = new UserDetailsAuthenticationProvider(passwordEncoder(), userService);
        return provider;
    }


    private ClientRegistration getRegistration(String client) {
        String clientId = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-id");

        if (clientId == null) {
            return null;
        }


        String clientSecret = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-secret");
        if (client.equals("google")) {
            return CommonOAuth2Provider.GOOGLE.getBuilder(client)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .build();
        }
        if (client.equals("facebook")) {
            return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .build();
        }
        return null;
    }

}
class ClientResources {

    @NestedConfigurationProperty
    private AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails();

    @NestedConfigurationProperty
    private ResourceServerProperties resource = new ResourceServerProperties();

    public AuthorizationCodeResourceDetails getClient() {
        return client;
    }

    public ResourceServerProperties getResource() {
        return resource;
    }
}
*/
