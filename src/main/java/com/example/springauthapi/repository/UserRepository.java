package com.example.springauthapi.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springauthapi.model.Usuario;

public interface UserRepository extends JpaRepository<Usuario, String> {
    public List<Usuario> findAll();

    public Usuario findByUuid(String uuid);
}
