package com.danjitalk.danjitalk.infrastructure.jooq;


import com.danjitalk.danjitalk.infrastructure.jooq.properties.JooqDataSourceProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(JooqDataSourceProperties.class)
@Component
public class JooqCodeGenerator {

    private final JooqDataSourceProperties jooqDataSourceProperties;

    public void codeGenerator() throws Exception {
        String profile = System.getProperty("spring.profiles.active", "local");

        String databaseName = "local".equals(profile) ? "org.jooq.meta.mariadb.MariaDBDatabase" : "org.jooq.meta.mysql.MySQLDatabase";

        String schemaName = "local".equals(profile) ? "test" : "danjitalk";

        Jdbc jdbcConfig = new Jdbc()
                .withDriver(jooqDataSourceProperties.getDriverClassName())
                .withUrl(jooqDataSourceProperties.getUrl())
                .withUser(jooqDataSourceProperties.getUsername())
                .withPassword(jooqDataSourceProperties.getPassword());

        GenerationTool.generate(new Configuration()
                .withJdbc(jdbcConfig)
                .withGenerator(new Generator()
                        .withDatabase(new Database()
                                .withName(databaseName)
                                .withIncludes("feed|reaction")
                                .withInputSchema(schemaName)
                                // enum 처리
                                .withForcedTypes(List.of(
                                        new ForcedType()
                                                .withUserType("com.danjitalk.danjitalk.domain.community.reaction.enums.ReactionType")
                                                .withIncludeExpression("reaction\\.reaction_type")
                                ))
                        )
                        .withTarget(new Target()
                                .withDirectory("src/main/java")
                                .withPackageName("com.danjitalk.danjitalk.infrastructure.jooq.table")
                        )));

    }
}
