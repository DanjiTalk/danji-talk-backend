package com.danjitalk.danjitalk.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;

@Getter
@Setter
@ConfigurationProperties(prefix = "aws")
@Configuration
public class S3Config {

    private String region;

    private Region regionEnum;

    private String bucketName;

    private String accessKey;

    private String secretKey;

    @PostConstruct
    public void init() {
        this.regionEnum = Region.of(region);
    }
}
