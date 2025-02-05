package com.esys.framework.zuul;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

@Component
@Primary
@EnableAutoConfiguration
public class DocumentationController implements SwaggerResourcesProvider {

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        resources.add(swaggerResource("organization-service", "/api/organization/v2/api-docs", "2.0"));
        resources.add(swaggerResource("message-service", "/api/message/v2/api-docs", "2.0"));
        resources.add(swaggerResource("uaa-service", "/api/uaa/v2/api-docs", "2.0"));
        resources.add(swaggerResource("base-service", "/api/base/v2/api-docs", "2.0"));
        return resources;
    }



    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }

}