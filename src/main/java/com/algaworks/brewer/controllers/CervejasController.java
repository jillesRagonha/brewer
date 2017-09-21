package com.algaworks.brewer.controllers;

import com.algaworks.brewer.models.Cerveja;
import com.algaworks.brewer.models.Origem;
import com.algaworks.brewer.models.Sabor;
import com.algaworks.brewer.repository.Cervejas;
import com.algaworks.brewer.repository.Estilos;
import com.algaworks.brewer.service.CadastroCervejaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/cervejas")
public class CervejasController {

    @Autowired
    private Cervejas cervejas;
    @Autowired
    private Estilos estilos;
    @Autowired
    private CadastroCervejaService service;

    private static final Logger logger = LoggerFactory.getLogger(CervejasController.class);

    //Método chamado pelo Spring quando eu fizer uma requisição do tipo GET
    @RequestMapping("/novo")
    public ModelAndView novo(Cerveja cerveja) {

        ModelAndView mv = new ModelAndView("cerveja/CadastroCerveja");
        mv.addObject("sabores", Sabor.values());
        mv.addObject("estilos", estilos.findAll());
        mv.addObject("origem", Origem.values());
        return mv;
    }

    //Método chamado pelo Spring quando eu fizer uma requisição do tipo POST na mesma url
    @RequestMapping(value = "/novo", method = RequestMethod.POST)
    public ModelAndView cadastrarCerveja(@Valid Cerveja cerveja, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return novo(cerveja);
        }
        service.salvar(cerveja);
        attributes.addFlashAttribute("mensagem", "Cerveja salva com sucesso - CUZAO");
        return new ModelAndView("redirect:/cervejas/novo");
    }

    @GetMapping
    public ModelAndView pesquisar(){
        ModelAndView modelAndView = new ModelAndView("cerveja/PesquisaCerveja");
        modelAndView.addObject("sabores", Sabor.values());
        modelAndView.addObject("estilos", estilos.findAll());
        modelAndView.addObject("origem", Origem.values());
        modelAndView.addObject("cervejas", cervejas.findAll());
        return  modelAndView;
    }

}
