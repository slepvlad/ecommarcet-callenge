package com.ecommarket.challenge.controller;

import com.ecommarket.challenge.annotation.IpRateLimit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/greeting")
public class SimpleController {

    @GetMapping
    @IpRateLimit
    public void dummyEndpoint() {
    }

}
