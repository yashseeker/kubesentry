package com.yash.kubesentry.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/test/cpu")
    public String cpuTest() {

        long sum = 0;

        for (long i = 0; i < 5_000_000_000L; i++) {
            sum += i;
        }

        return "Done " + sum;
    }
}