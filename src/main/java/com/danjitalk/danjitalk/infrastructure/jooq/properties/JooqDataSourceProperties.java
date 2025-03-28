package com.danjitalk.danjitalk.infrastructure.jooq.properties;

//@Getter
//@Setter
//@ConfigurationProperties(prefix = "spring")
public class JooqDataSourceProperties {

    public static class JooqDataProperties {
        private String datasourceUrl;
        private String datasourceUsername;
        private String datasourcePassword;
        private String datasourceDriverClassName;
    }

    public static class JooqProperties {

    }

}