package com.danjitalk.danjitalk.infrastructure.jooq.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring")
public class JooqProperties {

    private Datasource datasource;
    private Jooq jooq;

    @Getter @Setter
    public static class Datasource {
        private String url;
        private String username;
        private String password;
        private String driverClassName;
    }

    @Getter @Setter
    public static class Jooq {
        private String sql;
        private String schemaName;
    }

}
