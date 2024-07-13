package com.example.springauthapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springauthapi.repository.UserRepository;

@Service
public class UsuarioService {
    @Autowired
    UserRepository userRepository;

    public void newUser() {

    }
}
