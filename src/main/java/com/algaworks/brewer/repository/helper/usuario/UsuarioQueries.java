package com.algaworks.brewer.repository.helper.usuario;

import com.algaworks.brewer.models.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioQueries {
    public Optional<Usuario> porEmailEAtivo(String email);

    public List<String> permissoes(Usuario usuario);
}
