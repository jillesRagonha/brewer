package com.algaworks.brewer.security;

import com.algaworks.brewer.models.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UsuarioDoSistema extends User {

    private Usuario usuario;
    private static final Long serialVersionUID = 1L;

    public UsuarioDoSistema(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {

        super(usuario.getEmail(), usuario.getSenha(), authorities);
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
