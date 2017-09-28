package com.algaworks.brewer.controllers;

import com.algaworks.brewer.controllers.page.PageWrapper;
import com.algaworks.brewer.models.Estilo;
import com.algaworks.brewer.repository.Estilos;
import com.algaworks.brewer.repository.filter.EstiloFilter;
import com.algaworks.brewer.service.CadastroEstiloService;
import com.algaworks.brewer.service.exception.NomeEstiloJaCadastradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/estilos")
public class EstilosController {

    @Autowired
    private CadastroEstiloService service;
    @Autowired
    private Estilos estilos;


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

    @GetMapping
    public ModelAndView pesquisar(EstiloFilter filter, BindingResult result, @PageableDefault(size = 2) Pageable pageable, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("estilo/PesquisaEstilos");
        PageWrapper<Estilo> pageWrapper = new PageWrapper<>(estilos.filtrar(filter, pageable), request);
        modelAndView.addObject("pagina",pageWrapper);
        return modelAndView;
    }
}
