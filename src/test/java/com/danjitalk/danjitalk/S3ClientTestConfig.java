package com.danjitalk.danjitalk;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.services.s3.S3Client;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class S3ClientTestConfig {

    @Bean
    @Primary
    public S3Client s3Client() {
        return mock(S3Client.class);
    }
}
