package com.danjitalk.danjitalk.infrastructure.s3.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "aws.s3")
public class S3ConfigProperties {

    private String region;

    private String bucketName;

    private String accessKey;

    private String secretKey;
}
