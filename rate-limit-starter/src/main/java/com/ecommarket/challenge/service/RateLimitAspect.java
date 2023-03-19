package com.ecommarket.challenge.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

@Aspect
@Component
public class RateLimitAspect {

    private final Logger logger = LoggerFactory.getLogger(RateLimitAspect.class);

    private final RateLimitService rateLimitService;

    public RateLimitAspect(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Around("@annotation(com.ecommarket.challenge.annotation.RateLimit)")
    public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {

        var param = joinPoint.getSignature().toString();
        logger.info("Param: {}",param);

        if (!rateLimitService.limit(param)) {
            logger.warn("Limit is exceeded");
            throw new HttpServerErrorException(HttpStatus.BAD_GATEWAY);
        }
        return joinPoint.proceed();
    }

}
