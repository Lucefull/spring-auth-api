package com.example.springauthapi.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                return http.csrf().disable()
                                .authorizeHttpRequests(auth -> auth

                                                .requestMatchers("/v3/**", "/swagger-ui/**")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                                                .anyRequest()
                                                .authenticated())
                                .oauth2ResourceServer(oauth2 -> oauth2
                                                .jwt()
                                                .jwtAuthenticationConverter(grantedAuthoritiesExtractor()))
                                .cors(Customizer.withDefaults())
                                .build();
        }

        private Converter<Jwt, AbstractAuthenticationToken> grantedAuthoritiesExtractor() {
                JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
                jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
                                (Converter<Jwt, java.util.Collection<GrantedAuthority>>) new GrantedAuthoritiesExtractor());
                return jwtAuthenticationConverter;
        }

        public class GrantedAuthoritiesExtractor implements Converter<Jwt, Collection<GrantedAuthority>> {

                public Collection<GrantedAuthority> convert(Jwt jwt) {

                        Map<String, Collection<?>> resourceAcess = (Map<String, Collection<?>>) jwt.getClaims()
                                        .get("resource_access");

                        Map<String, Collection<?>> client = (Map<String, Collection<?>>) resourceAcess
                                        .get(GlobalValue.clientId);

                        return client
                                        .getOrDefault("roles", Collections.emptyList())
                                        .stream()
                                        .map(Object::toString)
                                        .map(SimpleGrantedAuthority::new)
                                        .collect(Collectors.toList());
                }
        }
}
