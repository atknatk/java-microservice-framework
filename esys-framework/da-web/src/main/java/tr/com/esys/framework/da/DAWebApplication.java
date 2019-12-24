package tr.com.esys.framework.da;

import com.esys.framework.core.configuration.EnableCoreConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCoreConfig
public class DAWebApplication {

    public static void main(String[] args) {

        SpringApplication.run(DAWebApplication.class, args);

    }
}
