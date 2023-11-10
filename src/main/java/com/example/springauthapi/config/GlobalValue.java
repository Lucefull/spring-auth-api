package com.example.springauthapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GlobalValue {

    public static String clientId;

    @Value("${env.keycloak.client-id}")
    public void setClientId(String value) {
        this.clientId = value;
    }
}
