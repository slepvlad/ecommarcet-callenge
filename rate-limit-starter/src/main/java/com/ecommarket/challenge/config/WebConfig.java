package com.ecommarket.challenge.config;

import com.ecommarket.challenge.service.ipratelimit.IpRateLimitInterceptor;
import com.ecommarket.challenge.property.RateLimitProperty;
import com.ecommarket.challenge.service.RateLimitService;
import com.ecommarket.challenge.service.ipratelimit.RateLimitServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties(RateLimitProperty.class)
public class WebConfig implements WebMvcConfigurer {

    private final RateLimitProperty rateLimitProperty;

    public WebConfig(RateLimitProperty rateLimitProperty) {
        this.rateLimitProperty = rateLimitProperty;
    }

    @Bean
    public RateLimitService rateLimitService() {
        return new RateLimitServiceImpl(rateLimitProperty);
    }


    @Bean
    public IpRateLimitInterceptor ipRateLimitInterceptor() {
        return new IpRateLimitInterceptor(rateLimitService());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipRateLimitInterceptor());
    }
}
