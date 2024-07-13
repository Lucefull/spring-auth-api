package com.example.springauthapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springauthapi.dto.LoginDto;
import com.example.springauthapi.dto.TokenDto;
import com.example.springauthapi.service.AuthenticationService;
import com.example.springauthapi.service.UsuarioService;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth", description = "API de autenticação")
@RestController
@RequestMapping("authController")
public class AuthController {

    @Autowired
    private AuthenticationService authService;

    @Autowired
    UsuarioService usuarioService;

    @PreAuthorize("hasAuthority('user')")
    @GetMapping("/user")
    public String user() {
        return authService.getUser();
    }

    @SecurityRequirements
    @PostMapping("/login")
    public TokenDto login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto.getUsername(), loginDto.getPassword());

    }
}
