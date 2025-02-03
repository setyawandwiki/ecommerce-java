package com.stwn.ecommerce_java.controller;

import com.stwn.ecommerce_java.common.errors.BadRequestException;
import com.stwn.ecommerce_java.common.errors.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping
    public String helloController(){
        return "hello world";
    }
    @GetMapping("/bad-request")
    public ResponseEntity<String> badRequestError(){
        throw new BadRequestException("ada error bad request");
    }
    @GetMapping("/generic-error")
    public ResponseEntity<String> genericError(){
        throw new RuntimeException("generic error");
    }
    @GetMapping("/not-found")
    public ResponseEntity<String> notFoundError(){
        throw new ResourceNotFoundException("data tidak ditemukan");
    }
}
