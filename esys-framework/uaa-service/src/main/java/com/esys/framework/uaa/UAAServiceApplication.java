package com.esys.framework.uaa;

import com.esys.framework.core.configuration.EnableCoreConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;


@SpringBootApplication
@EnableCoreConfig
public class UAAServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UAAServiceApplication.class, args);
    }
}
