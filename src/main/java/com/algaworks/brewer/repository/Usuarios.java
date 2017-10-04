package com.algaworks.brewer.repository;

import com.algaworks.brewer.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Usuarios extends JpaRepository<Usuario, Long> {

    public Optional<Usuario> findByEmailIgnoreCase(String email);

}
