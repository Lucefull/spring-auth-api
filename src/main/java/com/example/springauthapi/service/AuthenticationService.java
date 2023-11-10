package com.example.springauthapi.service;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.springauthapi.dto.TokenDto;

import net.minidev.json.JSONObject;

@Service
public class AuthenticationService {

    public String getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt userDetails = (Jwt) authentication.getPrincipal();
        String sub = userDetails.getClaim("preferred_username");
        return "clientId";
    }

    public TokenDto login() {
        String tokenUrl = "http://keycloak.com.au:8180/realms/archangel/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", "lucefull");
        map.add("password", "a.123456");
        map.add("grant_type", "password");
        map.add("client_id", "nest-auth-app");
        map.add("client_secret", "pSYktmVBYw5YZqeAeAWwLDPvN6w9Tp56");
        map.add("scope", "openid");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        RestTemplate restTemplate = new RestTemplate();
        TokenDto response = restTemplate.postForObject(tokenUrl, request, TokenDto.class);

        return response;
    }

    public TokenDto refreshToken(String token) {
        String tokenUrl = "http://keycloak.com.au:8180/realms/archangel/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", "nest-auth-app");
        map.add("grant_type", "refresh_token");
        map.add("refresh_token", token);
        map.add("client_secret", "DhRVLq8Zid1campDUX3B2ItyJXH4LwL7");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        RestTemplate restTemplate = new RestTemplate();
        TokenDto response = restTemplate.postForObject(tokenUrl, request, TokenDto.class);

        return response;
    }
}
