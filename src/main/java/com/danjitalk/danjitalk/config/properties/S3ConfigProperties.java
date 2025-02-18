package com.danjitalk.danjitalk.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "aws.s3")
@Configuration
public class S3ConfigProperties {

    private String region;

    private String bucketName;

    private String accessKey;

    private String secretKey;
}
