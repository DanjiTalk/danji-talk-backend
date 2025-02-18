package com.danjitalk.danjitalk.common.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtSecretKeys {

    private static String accessSecret;
    private static String refreshSecret;

    @Value("${jwt.secret.access}")
    private void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public static String getAccessSecret() {
        return accessSecret;
    }

    @Value("${jwt.secret.refresh}")
    private void setRefreshSecret(String refreshSecret) {
        this.refreshSecret = refreshSecret;
    }

    public static String getRefreshSecret() {
        return refreshSecret;
    }
}
