package com.esys.framework.core.configuration;

import com.esys.framework.core.consts.FieldSecurityConstants;
import com.esys.framework.core.entity.util.JSR310DateTimeSerializer;
import com.esys.framework.core.entity.util.JSR310LocalDateDeserializer;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.core.security.field.entity.EntityCreatedByProvider;
import com.esys.framework.core.security.field.exceptions.AccessDeniedExceptionHandler;
import com.esys.framework.core.security.field.filter.JacksonSecureFieldFilter;
import com.esys.framework.core.security.field.principal.PrincipalProvider;
import com.esys.framework.core.web.controller.ControllerTraceInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.time.*;
import java.util.List;

/*
@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false).
                favorParameter(true).
                parameterName("mediaType").
                ignoreAcceptHeader(true).
                useJaf(false).
                defaultContentType(MediaType.APPLICATION_JSON).
                mediaType("xml", MediaType.APPLICATION_XML).
                mediaType("json", MediaType.APPLICATION_JSON);
    }

    @Bean
    LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang"); // Query string parameter name
        registry.addInterceptor(localeChangeInterceptor);
        super.addInterceptors(registry);
    }

    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
  }
}
*/

@Configuration
@Profile("!test")
// @EnableWebMvc
@ComponentScan("com.esys")
public class WebConfig extends DelegatingWebMvcConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PrincipalProvider globalPrincipalProvider;

    @Autowired
    private EntityCreatedByProvider globalEntityCreatedByProvider;

    @Autowired
    private AccessDeniedExceptionHandler accessDeniedExceptionHandler;

    @Bean
    public HandlerInterceptor handlerInterceptor(){
        return new ControllerTraceInterceptor();
    };

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }


    @Override
    public void configureMessageConverters(final List<HttpMessageConverter<?>> messageConverters) {
        // Add a MappingJackson2HttpMessageConverter so that
        // objectMapper.writeFiltered
        // is using the objectMapper configured with the needed Mixin
        FilterProvider filters = new SimpleFilterProvider().addFilter(FieldSecurityConstants.SECURITY_FIELD_FILTER,
                new JacksonSecureFieldFilter(applicationContext,globalPrincipalProvider,globalEntityCreatedByProvider,
                        accessDeniedExceptionHandler));

        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(OffsetDateTime.class, JSR310DateTimeSerializer.INSTANCE);
        module.addSerializer(ZonedDateTime.class, JSR310DateTimeSerializer.INSTANCE);
        module.addSerializer(LocalDateTime.class, JSR310DateTimeSerializer.INSTANCE);
        module.addSerializer(Instant.class, JSR310DateTimeSerializer.INSTANCE);
        module.addDeserializer(LocalDate.class, JSR310LocalDateDeserializer.INSTANCE);

        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
                .json()
                .filters(filters)
                .mixIn(Object.class,ModelResult.class)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .findModulesViaServiceLoader(true)
                .modulesToInstall(module)
                .build();
        messageConverters.add(new MappingJackson2HttpMessageConverter(objectMapper));
        addDefaultHttpMessageConverters(messageConverters);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handlerInterceptor());
    }



}

