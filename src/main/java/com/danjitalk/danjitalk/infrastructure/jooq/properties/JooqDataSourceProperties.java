package com.danjitalk.danjitalk.infrastructure.jooq.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.datasource")
public class JooqDataSourceProperties {

    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
