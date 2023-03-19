package com.ecommarket.challenge;

import com.ecommarket.challenge.annotation.IpRateLimit;
import com.ecommarket.challenge.annotation.RateLimit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/greeting")
public class SimpleController {

    private final SimpleService service;

    public SimpleController(SimpleService service) {
        this.service = service;
    }

    @GetMapping
    @IpRateLimit
    public void dummyEndpoint() {
    }

    @GetMapping("/new")
    public void dummyEndpointNew() {
        service.action("sa;gljamkd ");
    }

}
