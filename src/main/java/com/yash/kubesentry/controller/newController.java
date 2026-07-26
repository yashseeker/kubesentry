package com.yash.kubesentry.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
public class newController {

    @GetMapping("/")
    public String home() {
        return "KubeSentry API is running 🚀";
    }

}

