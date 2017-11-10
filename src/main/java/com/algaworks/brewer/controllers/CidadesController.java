package com.algaworks.brewer.controllers;

import com.algaworks.brewer.controllers.page.PageWrapper;
import com.algaworks.brewer.models.Cidade;
import com.algaworks.brewer.repository.Cidades;
import com.algaworks.brewer.repository.Estados;
import com.algaworks.brewer.repository.filter.CidadeFilter;
import com.algaworks.brewer.service.CadastroCidadeService;
import com.algaworks.brewer.service.exception.ImpossivelExcluirEntidadeException;
import com.algaworks.brewer.service.exception.NomeCidadeJaCadastradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
import java.util.List;

@Controller
@RequestMapping("/cidades")
public class CidadesController {

    @Autowired
    private Cidades cidades;
    @Autowired
    private Estados estados;
    @Autowired
    private CadastroCidadeService cadastroCidadeService;

    @RequestMapping("/novo")
    public ModelAndView novo(Cidade cidade) {
        ModelAndView modelAndView = new ModelAndView("cidade/CadastroCidade");
        modelAndView.addObject("estados", estados.findAll());
        return modelAndView;
    }

    @Cacheable(value = "cidades", key = "#codigoEstado")
    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Cidade>  pesquisarPorCodigoEstado(@RequestParam(name = "estado", defaultValue = "-1") Long codigoEstado) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return cidades.findByEstadoCodigo(codigoEstado);
    }

    @PostMapping({"/novo", "{\\d+}"})
    @CacheEvict(value = "cidades", key = "#cidade.estado.codigo", condition = "#cidade.temEstado()")
    public ModelAndView salvar(@Valid Cidade cidade, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return novo(cidade);
        }

        try {
            cadastroCidadeService.salvar(cidade);
        }catch (NomeCidadeJaCadastradoException e){
            result.rejectValue("nome", e.getMessage(), e.getMessage());
            return novo(cidade);
        }

        attributes.addFlashAttribute("mensagem", "Cidade salva com sucesso");
        return new ModelAndView("redirect:/cidades/novo");

    }

    @GetMapping
    public ModelAndView pesquisar(CidadeFilter cidadeFilter, BindingResult result, @PageableDefault(size =5)Pageable pageable, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("cidade/PesquisaCidade");
        modelAndView.addObject("estados", estados.findAll());

        PageWrapper<Cidade> paginaWrapper = new PageWrapper<>(cidades.filtrar(cidadeFilter, pageable), request);
        modelAndView.addObject("pagina", paginaWrapper);

        return modelAndView;
    }

    @GetMapping("/{codigo}")
    public ModelAndView editar(@PathVariable Long codigo) {
        Cidade cidade = cidades.buscarComEstados(codigo);
        ModelAndView mv = novo(cidade);
        mv.addObject(cidade);
        return mv;
    }

    @DeleteMapping("/{codigo}")
    public @ResponseBody
    ResponseEntity<?> excluir(@PathVariable("codigo") Cidade cidade) {
        try {
            cadastroCidadeService.excluir(cidade);
        } catch (ImpossivelExcluirEntidadeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}


