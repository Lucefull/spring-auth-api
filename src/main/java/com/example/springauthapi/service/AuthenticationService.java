package com.example.springauthapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.example.springauthapi.dto.TokenDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AuthenticationService {

    @Value("${env.keycloak.url}")
    private String keycloakUrl;
    @Value("${env.keycloak.realm}")
    private String keycloakRealm;
    @Value("${env.keycloak.client-secret}")
    private String keycloakSecret;
    @Value("${env.keycloak.client-id}")
    private String keycloakClientId;

    public String getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt userDetails = (Jwt) authentication.getPrincipal();
        String sub = userDetails.getClaim("preferred_username");
        return sub;
    }

    public TokenDto login(String username, String password) throws Exception {
        String tokenUrl = String.format("%s/realms/%s/protocol/openid-connect/token", keycloakUrl, keycloakRealm);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);
        map.add("grant_type", "password");
        map.add("client_id", keycloakClientId);
        map.add("client_secret", keycloakSecret);
        map.add("scope", "openid");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        RestTemplate restTemplate = new RestTemplate();
        TokenDto response = restTemplate.postForObject(tokenUrl, request, TokenDto.class);

        return response;
    }

    public TokenDto refreshToken(String token) throws Exception {
        String tokenUrl = String.format("%s/realms/%s/protocol/openid-connect/token", keycloakUrl, keycloakRealm);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", keycloakClientId);
        map.add("grant_type", "refresh_token");
        map.add("refresh_token", token);
        map.add("client_secret", keycloakSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        RestTemplate restTemplate = new RestTemplate();
        TokenDto response = restTemplate.postForObject(tokenUrl, request, TokenDto.class);

        return response;
    }

    public void createNewUser() throws JsonProcessingException {
        String url = String.format("%s/admin/realms/%s/users", keycloakUrl, keycloakRealm);
        String token = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJrUXhnTXhQRnJGX3plZkFfdjFrajM1eWljWW56S0xpSTl0WDhkNC14cURZIn0.eyJleHAiOjE2OTk4MjYzNjksImlhdCI6MTY5OTgyNjA2OSwianRpIjoiYzcxMjQ0MTAtMzYzNi00Y2U3LWE0MjMtNTFjN2JiM2QwNDM2IiwiaXNzIjoiaHR0cDovL2tleWNsb2FrLmNvbS5hdTo4MTgwL3JlYWxtcy9hcmNoYW5nZWwiLCJhdWQiOlsicmVhbG0tbWFuYWdlbWVudCIsImJyb2tlciIsImFjY291bnQiXSwic3ViIjoiNTI3MGRjNTgtM2E0Mi00YjhhLWE3NzItMTdkYWMwMjRiYzg3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoibmVzdC1hdXRoLWFwcCIsInNlc3Npb25fc3RhdGUiOiIwYTk1M2VhNS01ZmYyLTQxYTQtYjM2Mi05YjI0ZjNmYWRjMmQiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIi8qIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJkZWZhdWx0LXJvbGVzLWFyY2hhbmdlbCIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJyZWFsbS1tYW5hZ2VtZW50Ijp7InJvbGVzIjpbInZpZXctcmVhbG0iLCJ2aWV3LWlkZW50aXR5LXByb3ZpZGVycyIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJpbXBlcnNvbmF0aW9uIiwicmVhbG0tYWRtaW4iLCJjcmVhdGUtY2xpZW50IiwibWFuYWdlLXVzZXJzIiwicXVlcnktcmVhbG1zIiwidmlldy1hdXRob3JpemF0aW9uIiwicXVlcnktY2xpZW50cyIsInF1ZXJ5LXVzZXJzIiwibWFuYWdlLWV2ZW50cyIsIm1hbmFnZS1yZWFsbSIsInZpZXctZXZlbnRzIiwidmlldy11c2VycyIsInZpZXctY2xpZW50cyIsIm1hbmFnZS1hdXRob3JpemF0aW9uIiwibWFuYWdlLWNsaWVudHMiLCJxdWVyeS1ncm91cHMiXX0sIm5lc3QtYXV0aC1hcHAiOnsicm9sZXMiOlsidW1hX3Byb3RlY3Rpb24iLCJhZG1pbiIsInVzZXIiXX0sImJyb2tlciI6eyJyb2xlcyI6WyJyZWFkLXRva2VuIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1hcHBsaWNhdGlvbnMiLCJ2aWV3LWNvbnNlbnQiLCJ2aWV3LWdyb3VwcyIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwiZGVsZXRlLWFjY291bnQiLCJtYW5hZ2UtY29uc2VudCIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIGVtYWlsIHByb2ZpbGUiLCJzaWQiOiIwYTk1M2VhNS01ZmYyLTQxYTQtYjM2Mi05YjI0ZjNmYWRjMmQiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6Imx1Y2VmdWxsIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.wADfgbIS0eFLv2UYUJsq9wsEWaO6ADvyKw9nbQTnEmj1N9fZpxM-pvkKPsTo1RH7topxP1ywxHcETe95Ex3KILxp16j8UzUWUItKqhuW2mXiLBjCDrNd4irwG0qXvWEZ1kFfgTY2RV2VSbzpL8iXuW1fFWBpxXL6KBbFOtAgk2k7SLCowbMlEZC6oKWN2NXc6Q_SIVoINkRHPD2E_4r_qjwnVXTVd9TLssKcV3cvUjOb0czETK4qSXLASJObuiOHlNu1OrX7FNxQ1i8VB8uJIuFE5RfQ3PCVl-rj2CdwjDZoQ75C-udpTMc_pQaS-Z0HffZiBZlKXdkB45L3wmfz6A";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode userNode = objectMapper.createObjectNode()
                .put("username", "app_user_test")
                .put("firstName", "app_user_firstname")
                .put("lastName", "app_user_lastname")
                .put("email", "app_user@test.fr")
                .put("emailVerified", true)
                .put("enabled", true)
                .set("credentials", objectMapper.createArrayNode()
                        .add(objectMapper.createObjectNode()
                                .put("temporary", false)
                                .put("type", "password")
                                .put("value", "mypassword")));

        // Convertendo para uma string para exibição
        String jsonString = objectMapper.writeValueAsString(userNode);
        System.out.println(jsonString);

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);

        System.out.println("Resposta do servidor: " + responseEntity.getStatusCode());
        System.out.println("Resposta do servidor (corpo): " + responseEntity.getBody());

    }

    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt userDetails = (Jwt) authentication.getPrincipal();
        String sub = userDetails.getClaim("sub");

        // {{keycloak_url}}/admin/realms/{{realm}}/users/{{userId}}/logout
        String url = String.format("%s/admin/realms/%s/users/%s/logout", keycloakUrl, keycloakRealm, sub);
        System.out.println(url);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userDetails.getTokenValue());

        // Criar instância do RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Fazer a requisição POST void
        restTemplate.postForLocation(url, null, headers);
    }
}
