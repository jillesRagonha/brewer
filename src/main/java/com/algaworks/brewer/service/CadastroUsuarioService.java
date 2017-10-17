package com.algaworks.brewer.service;

import com.algaworks.brewer.models.Usuario;
import com.algaworks.brewer.repository.Usuarios;
import com.algaworks.brewer.service.exception.EmailUsuarioJaCadastradoException;
import com.algaworks.brewer.service.exception.SenhaObrigatoriaUsuarioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class CadastroUsuarioService {

    @Autowired
    private Usuarios usuarios;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario salvar(Usuario usuario) {
        Optional<Usuario> usuarioOptional = usuarios.findByEmailIgnoreCase(usuario.getEmail());
        if (usuarioOptional.isPresent()) {
            throw new EmailUsuarioJaCadastradoException("Esse email já esta cadastrado no banco de dados");
        }

        if (usuario.isNovoUsuario() && StringUtils.isEmpty(usuario.getSenha())) {
            throw new SenhaObrigatoriaUsuarioException("Preencha a senha");
        }

        if (usuario.isNovoUsuario()) {
            usuario.setSenha(this.passwordEncoder.encode(usuario.getSenha()));
            usuario.setConfirmacaoSenha(usuario.getSenha());
        }
        return usuarios.saveAndFlush(usuario);
    }

    @Transactional
    public void alterarStatus(Long[] codigos, StatusUsuario statusUsuario) {
        statusUsuario.executar(codigos,usuarios);
    }
}
