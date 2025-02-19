package com.danjitalk.danjitalk.config;

import com.danjitalk.danjitalk.config.properties.S3ConfigProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(S3ConfigProperties.class)
@Slf4j
public class AwsConfig {

    private final S3ConfigProperties s3ConfigProperties;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(s3ConfigProperties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(s3ConfigProperties.getAccessKey(), s3ConfigProperties.getSecretKey())
                ))
                .endpointOverride(URI.create("https://s3.ap-northeast-2.amazonaws.com"))    // 리전이 서울이면
                .build();
    }

    @PostConstruct
    public void init() {
        log.info("🔥 AWS S3 Configuration Initialized");
        log.info("✅ S3 Region: {}", s3ConfigProperties.getRegion());
        log.info("✅ S3 Access Key: {}", s3ConfigProperties.getAccessKey() != null ? "SET" : "NOT SET");
        log.info("✅ S3 Secret Key: {}", s3ConfigProperties.getSecretKey() != null ? "SET" : "NOT SET");
    }
}
