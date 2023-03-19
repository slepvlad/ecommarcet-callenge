package com.ecommarket.challenge;


import com.ecommarket.challenge.annotation.RateLimit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SimpleService {

    private final Logger log = LoggerFactory.getLogger(SimpleService.class);

    @RateLimit
    public void action(String arg){
        log.info("Dummy service");
    }
}
