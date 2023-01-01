package com.example.spring_security.jwt;

import com.google.common.net.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${application.jwt.secretKey}")
    private String secretKey;
    @Value("${application.jwt.tokenPrefix}")
    private String tokenPrefix;
    @Value("${application.jwt.tokenExpirationAfterDays}")
    private Integer tokenExpirationAfterDays;

    public JwtConfig() {
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public Integer getTokenExpirationAfterDays() {
        return tokenExpirationAfterDays;
    }

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }
}
