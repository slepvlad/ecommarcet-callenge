package com.ecommarket.challenge.service;


import com.ecommarket.challenge.annotation.IpRateLimit;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

import static java.lang.String.format;

public class IpRateLimitInterceptor implements HandlerInterceptor {

    private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"};

    private final RateLimitService rateLimitService;

    public IpRateLimitInterceptor(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        final IpRateLimit ipRateLimit = ((HandlerMethod) handler).getMethod().getAnnotation((IpRateLimit.class));
        if (Objects.isNull(ipRateLimit)) {
            return true;
        }
        var ipAddress = getClientIpAddress(request);
        var param = format("%s/%s:%s", request.getMethod(), request.getRequestURI(), ipAddress);
        if (!rateLimitService.limit(param)) {
            throw new HttpServerErrorException(HttpStatus.BAD_GATEWAY);
        }
        return true;
    }

    private String getClientIpAddress(HttpServletRequest request) {
        for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }
}
