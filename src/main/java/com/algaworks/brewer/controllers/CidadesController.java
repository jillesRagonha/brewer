package com.algaworks.brewer.controllers;

import com.algaworks.brewer.models.Cidade;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CidadesController {

    @RequestMapping("/cidades/novo")
    public String novo(Cidade cidade) {
        return "cidade/CadastroCidade";
    }
}
