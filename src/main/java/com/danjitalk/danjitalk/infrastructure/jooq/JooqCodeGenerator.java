package com.danjitalk.danjitalk.infrastructure.jooq;


import lombok.extern.slf4j.Slf4j;
import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class JooqCodeGenerator {

    public static void codeGenerator() throws Exception {
        String profile = System.getProperty("spring.profiles.active", "local");
        String configFile = "application-" + profile + ".yml";

        String databaseName = "local".equals(profile) ? "org.jooq.meta.mariadb.MariaDBDatabase" : "org.jooq.meta.mysql.MySQLDatabase";

        // 추가 필요
        String schemaName = "local".equals(profile) ? "test" : "추가 필요";

        Properties properties = new Properties();

        try(InputStream inputStream = JooqCodeGenerator.class.getClassLoader().getResourceAsStream(configFile)) {

            if(inputStream == null) {
                throw new RuntimeException("Cannot find config file " + configFile);
            }

            properties.load(inputStream);

        } catch (IOException ioe) {
            throw new RuntimeException("ioe" + configFile, ioe);
        }

        Jdbc jdbcConfig = new Jdbc()
                .withDriver(properties.getProperty("spring.datasource.driver-class-name"))
                .withUrl(properties.getProperty("spring.datasource.url"))
                .withUser(properties.getProperty("spring.datasource.username"))
                .withPassword(properties.getProperty("spring.datasource.password"));

        GenerationTool.generate(new Configuration()
                .withJdbc(jdbcConfig)
                .withGenerator(new Generator()
                        .withDatabase(new Database()
                                .withName(databaseName)
                                .withIncludes("feed|reaction")
                                .withInputSchema(schemaName))
                        .withTarget(new Target()
                                .withPackageName("com.danjitalk.danjitalk.generated")
                                .withDirectory("build/generated/sources/jooq"))));

        log.info("Jooq code generation finished.");
    }
}
