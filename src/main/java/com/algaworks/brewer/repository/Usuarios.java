package com.algaworks.brewer.repository;

import com.algaworks.brewer.models.Usuario;
import com.algaworks.brewer.repository.helper.usuario.UsuarioQueries;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface Usuarios extends JpaRepository<Usuario, Long>, UsuarioQueries {

    public Optional<Usuario> findByEmailIgnoreCase(String email);

    public List<Usuario> findByCodigoIn(Long[] codigos);

}
