package com.example.springauthapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springauthapi.dto.CreateUserDto;
import com.example.springauthapi.dto.LoginDto;
import com.example.springauthapi.dto.TokenDto;
import com.example.springauthapi.dto.UserDto;
import com.example.springauthapi.service.AuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth", description = "API de autenticação")
@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthenticationService authService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user")
    public String user() {
        return authService.getUser();
    }

    @PostMapping("")
    public void newUser() {
        try {
            authService.createNewUser();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @SecurityRequirements
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto req) {
        try {
            return new ResponseEntity<TokenDto>(authService.login(req.getUserName(), req.getPassword()),
                    HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(500));
        }

    }

    @SecurityRequirements
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(String token) {
        try {
            return new ResponseEntity<TokenDto>(authService.refreshToken(token),
                    HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(500));
        }

    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public void logout() {
        authService.logout();
    }
}
