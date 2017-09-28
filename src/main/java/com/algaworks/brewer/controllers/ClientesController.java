package com.algaworks.brewer.controllers;

import com.algaworks.brewer.models.Cliente;
import com.algaworks.brewer.models.TipoPessoa;
import com.algaworks.brewer.repository.Estados;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/clientes")
public class ClientesController {

    @Autowired
    private Estados estados;

    @RequestMapping("novo")
    public ModelAndView novo(Cliente cliente) {
        ModelAndView modelAndView = new ModelAndView("cliente/CadastroCliente");
        modelAndView.addObject("tiposPessoa" ,TipoPessoa.values());
        modelAndView.addObject("estados", estados.findAll());
        return modelAndView;
    }

    @PostMapping("/novo")
    public ModelAndView salvar(@Valid Cliente cliente, BindingResult result) {
        if (result.hasErrors()) {
            return novo(cliente);
        }


        //TODO adicionar mensagem e salvar ou vice versa
        return new ModelAndView("redirect:/clientes/novo");
    }
}
