package com.api.mongodb.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "springfox.documentation.swagger.v2")
public class PropertiesEnvConfig {

    private String title;
    private String description;
    private String version;

}
