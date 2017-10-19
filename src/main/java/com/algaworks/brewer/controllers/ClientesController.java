package com.algaworks.brewer.controllers;

import com.algaworks.brewer.controllers.page.PageWrapper;
import com.algaworks.brewer.models.Cliente;
import com.algaworks.brewer.models.TipoPessoa;
import com.algaworks.brewer.repository.Clientes;
import com.algaworks.brewer.repository.Estados;
import com.algaworks.brewer.repository.filter.ClienteFilter;
import com.algaworks.brewer.service.CadastroClienteService;
import com.algaworks.brewer.service.exception.CpfClienteJaCadastradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClientesController {

    @Autowired
    private Estados estados;

    @Autowired
    private Clientes clientes;

    @Autowired
    private CadastroClienteService clienteService;

    @RequestMapping("/novo")
    public ModelAndView novo(Cliente cliente) {
        ModelAndView modelAndView = new ModelAndView("cliente/CadastroCliente");
        modelAndView.addObject("tiposPessoa" ,TipoPessoa.values());
        modelAndView.addObject("estados", estados.findAll());
        return modelAndView;
    }

    @PostMapping("/novo")
    public ModelAndView salvar(@Valid Cliente cliente, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return novo(cliente);
        }

        try {
            clienteService.salvar(cliente);

        } catch (CpfClienteJaCadastradoException e) {
            result.rejectValue("cpfOuCnpj", e.getMessage(), e.getMessage());
            return novo(cliente);
        }


        attributes.addFlashAttribute("mensagem", "Cliente salvo com sucesso");
        return new ModelAndView("redirect:/clientes/novo");
    }

    @GetMapping
    public ModelAndView pesquisar(ClienteFilter clienteFilter, BindingResult result, @PageableDefault(size = 3)Pageable pageable, HttpServletRequest servletRequest) {
        ModelAndView modelAndView = new ModelAndView("cliente/PesquisaCliente");

        PageWrapper<Cliente> paginaWrapper = new PageWrapper<>(clientes.filtrar(clienteFilter, pageable), servletRequest);

        modelAndView.addObject("pagina", paginaWrapper);
        return modelAndView;

    }

    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody  List<Cliente> pesquisar(String nome) {

        validarTamanhoNome(nome);
        return clientes.findByNomeIsContainingIgnoreCase(nome);
    }

    private void validarTamanhoNome(String nome) {
        if (StringUtils.isEmpty(nome) || nome.length() < 3) {
            throw new IllegalArgumentException();
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> tratarIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }

}
