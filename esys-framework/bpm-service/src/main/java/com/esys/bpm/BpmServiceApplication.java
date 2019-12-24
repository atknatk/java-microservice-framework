package com.esys.bpm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class BpmServiceApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BpmServiceApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(BpmServiceApplication.class, args);
	}

}

/*
 * @SpringBootApplication public class BpmServiceApplication {
 * 
 * public static void main(String[] args) {
 * SpringApplication.run(BpmServiceApplication.class, args); }
 * 
 * }
 */
