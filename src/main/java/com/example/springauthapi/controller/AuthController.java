package com.example.springauthapi.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth", description = "API de autenticação")
@RestController
@RequestMapping("auth")
public class AuthController {
    @GetMapping()
    public String teste() {
        return "hello world";
    }

    @PreAuthorize("hasAuthority('admin')")
    // @PreAuthorize("isAuthenticated()")
    @GetMapping("/user")
    public String user() {
        return "HI USER";
    }
}
