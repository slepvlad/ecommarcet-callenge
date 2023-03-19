package com.ecommarket.challenge;

import com.ecommarket.challenge.config.WebConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({WebConfig.class})
public @interface EnableIpRateLimit {
}
