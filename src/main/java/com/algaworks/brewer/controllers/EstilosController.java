package com.algaworks.brewer.controllers;

import com.algaworks.brewer.models.Estilo;
import com.algaworks.brewer.service.CadastroEstiloService;
import com.algaworks.brewer.service.exception.NomeEstiloJaCadastradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class EstilosController {

    @Autowired
    private CadastroEstiloService service;


    @RequestMapping("/estilos/novo")
    public ModelAndView novo(Estilo estilo) {
        ModelAndView mv = new ModelAndView("estilo/CadastroEstilo");

        return mv;
    }

    @RequestMapping(value = "/estilos/novo", method = RequestMethod.POST)
    public ModelAndView cadastrarNovoEstilo(@Valid Estilo estilo, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return novo(estilo);
        }
        try {
            service.salvar(estilo);
        } catch (NomeEstiloJaCadastradoException exception) {
            result.rejectValue("nome", exception.getMessage(),exception.getMessage());
            return novo(estilo);
        }
        attributes.addFlashAttribute("mensagem", "Novo estilo salvo com sucesso");
        return new ModelAndView("redirect:/estilos/novo");

    }
}
