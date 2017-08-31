package com.algaworks.brewer.controllers;

import com.algaworks.brewer.models.Cliente;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClientesController {

    @RequestMapping("/clientes/novo")
    public String novo(Cliente cliente) {
        return "cliente/CadastroCliente";
    }
}
