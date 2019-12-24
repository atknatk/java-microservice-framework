package com.esys.bpm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {

		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build()
				.tags(new Tag("businessRuleTask", "Operations specific to Business Rule Task in BPM"),
						new Tag("common", "General operations which will possibly be needed in BPM project"),
						new Tag("gatewayTask", "Operations specific to Exclusive, Inclusive, Parallel Gateways in BPM"),
						new Tag("instance", "Operations specific to Instances in BPM"),
						new Tag("notificationTask",
								"Operations specific to Participant which will be used in notification task, user task and dashboard pages in BPM"),
						new Tag("participant", "Operations specific to Participants in BPM"),
						new Tag("process", "Operations specific to Process in BPM"),
						new Tag("scriptTask", "Operations specific to Script Task in BPM"),
						new Tag("serviceTask", "Operations specific to Service Task in BPM"),
						new Tag("sqlTask", "Operations specific to Business Sql Task in BPM"),
						new Tag("timerTask", "Operations specific to Business Timer Task in BPM"),
						new Tag("userTask", "Operations specific to User Task in BPM"));

	}
}
