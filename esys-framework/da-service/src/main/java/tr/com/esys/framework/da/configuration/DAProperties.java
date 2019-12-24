package tr.com.esys.framework.da.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "da", ignoreUnknownFields = true)
public class DAProperties {

}
