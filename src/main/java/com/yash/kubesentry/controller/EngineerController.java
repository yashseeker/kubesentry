package com.yash.kubesentry.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/engineer")
public class EngineerController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ENGINEER')")
    public String dashboard() {
        return "Welcome Engineer";
    }
}