package com.ecommarket.challenge.config;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<Void> runtimeIllegalArgument(HttpServerErrorException exception) {
        return ResponseEntity
                .status(exception.getStatusCode())
                .build();
    }
}
