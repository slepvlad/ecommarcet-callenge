package com.ecommarket.challenge.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "rate-limit")
public record RateLimitProperty(long permit, Duration duration) {
}
