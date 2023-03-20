package com.ecommarket.challenge.service.ipratelimit;

import com.ecommarket.challenge.model.TokenBucket;
import com.ecommarket.challenge.property.RateLimitProperty;
import com.ecommarket.challenge.service.RateLimitService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitServiceImpl implements RateLimitService {

    private final Map<String, TokenBucket> cache = new ConcurrentHashMap<>();
    private final RateLimitProperty rateLimitProperty;

    public RateLimitServiceImpl(RateLimitProperty rateLimitProperty) {
        this.rateLimitProperty = rateLimitProperty;
    }


    @Override
    public boolean isLimit(String param) {
        var bucket = cache.get(param);
        if (bucket == null) {
            synchronized (this) {
                if (bucket == null) {
                    bucket = new TokenBucket(rateLimitProperty.permit(), rateLimitProperty.duration());
                    cache.put(param, bucket);
                }
            }
        }
        return !bucket.tryConsume(1);
    }
}
