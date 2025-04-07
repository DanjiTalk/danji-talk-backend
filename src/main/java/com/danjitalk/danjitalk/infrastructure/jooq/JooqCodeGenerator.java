package com.danjitalk.danjitalk.infrastructure.jooq;


import com.danjitalk.danjitalk.infrastructure.jooq.properties.JooqProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(JooqProperties.class)
@Component
public class JooqCodeGenerator {

    private final JooqProperties jooqProperties;

    public void codeGenerator() throws Exception {

        Jdbc jdbcConfig = new Jdbc()
                .withDriver(jooqProperties.getDatasource().getDriverClassName())
                .withUrl(jooqProperties.getDatasource().getUrl())
                .withUser(jooqProperties.getDatasource().getUsername())
                .withPassword(jooqProperties.getDatasource().getPassword());

        GenerationTool.generate(new Configuration()
                .withJdbc(jdbcConfig)
                .withGenerator(new Generator()
                        .withDatabase(new Database()
                                .withName(jooqProperties.getJooq().getSql())
                                .withIncludes("feed|reaction|bookmark")
                                .withInputSchema(jooqProperties.getJooq().getSchemaName())
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
