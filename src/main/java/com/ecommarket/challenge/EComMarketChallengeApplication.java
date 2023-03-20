package com.ecommarket.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableIpRateLimit
public class EComMarketChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EComMarketChallengeApplication.class, args);
    }

}
