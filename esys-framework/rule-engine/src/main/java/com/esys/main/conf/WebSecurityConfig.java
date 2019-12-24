package com.esys.main.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.esys.main.filter.JwtIntegrationKeyFilter;
import com.esys.main.security.TokenHelper;




@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

	@Autowired
	TokenHelper tokenHelper;

    @Autowired
    private MessageSource messageSource; 
    
	@Configuration
    @Order(1)
	public class RestAuthenticationConfig extends WebSecurityConfigurerAdapter {

 

    @Override
    protected void configure(HttpSecurity http) throws Exception {
                  
        http	
        
        		.antMatcher("/rest/**")
        		.addFilterBefore(new JwtIntegrationKeyFilter(tokenHelper), BasicAuthenticationFilter.class);

        
        http.csrf().disable();
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        // TokenAuthenticationFilter will ignore the below paths
        web.ignoring().antMatchers(
                HttpMethod.GET,
                "/resources/**",
                "/**/libs/**",
                "/*.html",
                "/favicon.ico",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js",
                "/**/**/*.js",
                "/**/**/*.html",
                "/**/**/**/*.png",
                "/**/**/**/*.css",
                "/**/**/**/*.css.map",
                "/**/**/**/*.eot",
                "/**/**/**/*.svg",
                "/**/**/**/*.ttf",
                "/**/**/**/*.woff",
                "/**/**/**/*.woff2",
                "/**/**/**/*.otf",
                "/**/**/**/*.js",
                "/**/**/**/*.html",
                "/**/**/**/*.scss",
                "/**/**/**/**/*.html",
                "/**/**/**/**/*.js",
                "/**/**/**/**/*.scss",
                "/**/**/**/**/**/*.html"
            );
    	}
	}

    @Configuration
    @Order(2)
	public class IntegrationAuthenticationConfig extends WebSecurityConfigurerAdapter {

	
	    @Autowired
	    TokenHelper tokenHelper;
	    
	     
	
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	                  
	        http	
	        .antMatcher("/integration/**")
	    	.addFilterBefore(new JwtIntegrationKeyFilter(tokenHelper),BasicAuthenticationFilter.class);
	            
	        http.csrf().disable();
	    }
	
	
	    @Override
	    public void configure(WebSecurity web) throws Exception {
	        // TokenAuthenticationFilter will ignore the below paths
	    	
	    	//Bu kisim test amacli konuldu kaldirilmali
	    	web.ignoring().antMatchers(
	    			"/integration/integrationController/generateIntegration"   
	            );
	    }
	}
    
	
}

