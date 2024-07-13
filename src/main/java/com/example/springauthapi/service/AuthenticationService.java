package com.example.springauthapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.example.springauthapi.dto.TokenDto;

@Service
public class AuthenticationService {

    @Value("${env.keycloak.url}")
    String url;
    @Value("${env.keycloak.realm}")
    String realm;
    @Value("${env.keycloak.client-secret}")
    String clientSecret;
    @Value("${env.keycloak.client-id}")
    String clientId;

    public String getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt userDetails = (Jwt) authentication.getPrincipal();
        String sub = userDetails.getClaim("preferred_username");
        return sub;
    }

    public TokenDto login(String username, String password) {

        String tokenUrl = url + "/realms/" + realm + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);
        map.add("grant_type", "password");
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("scope", "openid");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        RestTemplate restTemplate = new RestTemplate();
        TokenDto response = restTemplate.postForObject(tokenUrl, request, TokenDto.class);

        return response;
    }

    public TokenDto refreshToken(String token) {
        String tokenUrl = url + "/realms/" + realm + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("grant_type", "refresh_token");
        map.add("refresh_token", token);
        map.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        RestTemplate restTemplate = new RestTemplate();
        TokenDto response = restTemplate.postForObject(tokenUrl, request, TokenDto.class);

        return response;
    }
}
