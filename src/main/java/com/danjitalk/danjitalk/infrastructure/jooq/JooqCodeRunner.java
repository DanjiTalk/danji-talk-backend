package com.danjitalk.danjitalk.infrastructure.jooq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(1)
public class JooqCodeRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {

        try {
            log.info("CodeRunner : JooqCodeGenerate Started");
            JooqCodeGenerator.codeGenerator();
            log.info("CodeRunner : JooqCodeGenerate Finished");
        } catch(Exception e) {
            log.error("JooqCodeGenerate Error", e);
        }

    }
}
