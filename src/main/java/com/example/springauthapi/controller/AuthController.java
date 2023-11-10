package com.example.springauthapi.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springauthapi.dto.TokenDto;
import com.example.springauthapi.service.AuthenticationService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth", description = "API de autenticação")
@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthenticationService authService;

    @GetMapping()
    public String teste() {
        return "hello world";
    }

    // @PreAuthorize("hasAuthority('admin')")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user")
    public String user() {
        return authService.getUser();
    }

    @SecurityRequirements
    @PostMapping("/login")
    public TokenDto login() {
        return authService.login();

    }
}
