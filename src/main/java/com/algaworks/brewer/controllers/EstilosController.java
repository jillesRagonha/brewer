package com.algaworks.brewer.controllers;

import com.algaworks.brewer.models.Estilo;
import com.algaworks.brewer.service.CadastroEstiloService;
import com.algaworks.brewer.service.exception.NomeEstiloJaCadastradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/estilos")
public class EstilosController {

    @Autowired
    private CadastroEstiloService service;


    @RequestMapping("/novo")
    public ModelAndView novo(Estilo estilo) {
        ModelAndView mv = new ModelAndView("estilo/CadastroEstilo");

        return mv;
    }

    @RequestMapping(value = "/novo", method = RequestMethod.POST)
    public ModelAndView cadastrarNovoEstilo(@Valid Estilo estilo, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return novo(estilo);
        }
        try {
            service.salvar(estilo);
        } catch (NomeEstiloJaCadastradoException exception) {
            result.rejectValue("nome", exception.getMessage(), exception.getMessage());
            return novo(estilo);
        }
        attributes.addFlashAttribute("mensagem", "Novo estilo salvo com sucesso");
        return new ModelAndView("redirect:/estilos/novo");

    }

    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseEntity<?> salvar(@RequestBody @Valid Estilo estilo, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError("nome").getDefaultMessage());
        }
        estilo = service.salvar(estilo);

        return ResponseEntity.ok(estilo);


    }
}
