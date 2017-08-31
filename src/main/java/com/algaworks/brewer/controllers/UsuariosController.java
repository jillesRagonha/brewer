package com.algaworks.brewer.controllers;

import com.algaworks.brewer.models.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UsuariosController {

    @RequestMapping("/usuarios/novo")
    public String novo(Usuario usuario) {
        return "usuario/CadastroUsuario";
    }
}
