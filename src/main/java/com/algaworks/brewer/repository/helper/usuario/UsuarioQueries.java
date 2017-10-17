package com.algaworks.brewer.repository.helper.usuario;

import com.algaworks.brewer.models.Usuario;
import com.algaworks.brewer.repository.filter.UsuarioFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UsuarioQueries {
    public Optional<Usuario> porEmailEAtivo(String email);

    public List<String> permissoes(Usuario usuario);

    public Page<Usuario> filtrar(UsuarioFilter filtro, Pageable pageable);
}
