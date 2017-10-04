package com.algaworks.brewer.service;

import com.algaworks.brewer.models.Usuario;
import com.algaworks.brewer.repository.Usuarios;
import com.algaworks.brewer.service.exception.EmailUsuarioJaCadastradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CadastroUsuarioService {

    @Autowired
    private Usuarios usuarios;

    @Transactional
    public Usuario salvar(Usuario usuario) {
        Optional<Usuario> usuarioOptional = usuarios.findByEmailIgnoreCase(usuario.getEmail());
        if (usuarioOptional.isPresent()) {
            throw new EmailUsuarioJaCadastradoException("Esse email já esta cadastrado no banco de dados");
        }
        return usuarios.saveAndFlush(usuario);
    }
}
