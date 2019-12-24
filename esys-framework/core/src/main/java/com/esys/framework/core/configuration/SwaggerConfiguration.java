package com.esys.framework.core.configuration;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//@Profile(Constants.SPRING_PROFILE_DEVELOPMENT)
@EnableSwagger2
@Configuration
@Profile("!test")
public class SwaggerConfiguration {

    @Autowired
    private ServletContext servletContext;


    @Bean
    public Docket api() throws IOException, XmlPullParserException {
        MavenXpp3Reader reader = new MavenXpp3Reader();
       // Model model = reader.read(new FileReader("pom.xml"));
        List<SecurityScheme> schemeList = new ArrayList<>();
        schemeList.add(new ApiKey(HttpHeaders.AUTHORIZATION, "JWT", "header"));


        return new Docket(DocumentationType.SWAGGER_2)
             /*   .pathProvider(new RelativePathProvider(servletContext) {
                    @Override
                    public String getApplicationBasePath() {
                        if(servletContext.getContextPath() == "uaa"){
                            return servletContext.getContextPath();
                        }else if(servletContext.getContextPath() == "organization"){
                            return "../";
                        }else{
                            return "/";
                        }

                    }
                })*/
               // .pathMapping(servletContext.getContextPath())
                .securitySchemes(schemeList)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.esys.framework"))
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfo("Api Documentation", "Documentation automatically generated", "1.0.0", null, new Contact("Esys Bilişim", "https://isisbilisim.com.tr", "info@isisbilisim.com.tr"), null, null));
    }
}
