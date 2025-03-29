package com.danjitalk.danjitalk.infrastructure.jooq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JooqCodeRunner implements CommandLineRunner {

    private final JooqCodeGenerator jooqCodeGenerator;

    @Override
    public void run(String... args) {

        try {
            log.info("CodeRunner : JooqCodeGenerate Started");
            jooqCodeGenerator.codeGenerator();
            log.info("CodeRunner : JooqCodeGenerate Finished");
        } catch(Exception e) {
            log.error("JooqCodeGenerate Error", e);
        }

    }
}
