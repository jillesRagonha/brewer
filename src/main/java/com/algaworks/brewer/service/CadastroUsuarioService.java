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
    public void salvar(Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarios.findByEmailIgnoreCase(usuario.getEmail());
        if (usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)) {
            throw new EmailUsuarioJaCadastradoException("E-mail já cadastrado");
        }

        if (usuario.isNovoUsuario() && StringUtils.isEmpty(usuario.getSenha())) {
            throw new SenhaObrigatoriaUsuarioException("Senha é obrigatória para novo usuário");
        }

        if (usuario.isNovoUsuario() || !StringUtils.isEmpty(usuario.getSenha())) {
            usuario.setSenha(this.passwordEncoder.encode(usuario.getSenha()));
        } else if (StringUtils.isEmpty(usuario.getSenha())) {
            usuario.setSenha(usuarioExistente.get().getSenha());
        }
        usuario.setConfirmacaoSenha(usuario.getSenha());

        if (!usuario.isNovoUsuario() && usuario.getAtivo() == null) {
            usuario.setAtivo(usuarioExistente.get().getAtivo());
        }

        usuarios.save(usuario);
    }

    @Transactional
    public void alterarStatus(Long[] codigos, StatusUsuario statusUsuario) {
        statusUsuario.executar(codigos, usuarios);
    }
}
