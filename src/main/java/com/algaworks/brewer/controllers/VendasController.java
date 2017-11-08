package com.algaworks.brewer.controllers;

import com.algaworks.brewer.controllers.page.PageWrapper;
import com.algaworks.brewer.controllers.validator.VendaValidator;
import com.algaworks.brewer.mail.Mailer;
import com.algaworks.brewer.models.Cerveja;
import com.algaworks.brewer.models.StatusVenda;
import com.algaworks.brewer.models.TipoPessoa;
import com.algaworks.brewer.models.Venda;
import com.algaworks.brewer.repository.Cervejas;
import com.algaworks.brewer.repository.Vendas;
import com.algaworks.brewer.repository.filter.VendaFilter;
import com.algaworks.brewer.security.UsuarioDoSistema;
import com.algaworks.brewer.service.CadastroVendaService;
import com.algaworks.brewer.session.TabelaItensSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
@RequestMapping("/vendas")
public class VendasController {

    @Autowired
    private Mailer mailer;
    @Autowired
    private Cervejas cervejas;
    @Autowired
    private TabelaItensSession tabelaItens;
    @Autowired
    private CadastroVendaService vendaService;
    @Autowired
    private Vendas vendas;
    @Autowired
    private VendaValidator vendaValidator;

    @InitBinder("venda")
    public void inicializarValidador(WebDataBinder binder) {
        binder.setValidator(vendaValidator);
    }


    @GetMapping("/nova")
    public ModelAndView nova(Venda venda) {
        ModelAndView mv = new ModelAndView("venda/CadastroVenda");

        if (StringUtils.isEmpty(venda.getUuid())) {
            venda.setUuid(UUID.randomUUID().toString());
        }

        mv.addObject("itens", venda.getItens());
        mv.addObject("valorFrete", venda.getValorFrete());
        mv.addObject("valorDesconto", venda.getValorDesconto());
        mv.addObject("valorTotalItens", tabelaItens.getValorTotal(venda.getUuid()));

        return mv;
    }

    @PostMapping(value = "/nova", params = "salvar")
    public ModelAndView salvar(Venda venda, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioDoSistema usuarioSistema) {
        validarVenda(venda, result);
        if (result.hasErrors()) {
            return nova(venda);
        }

        venda.setUsuario(usuarioSistema.getUsuario());

        vendaService.salvar(venda);
        attributes.addFlashAttribute("mensagem", "Venda salva com sucesso");
        return new ModelAndView("redirect:/vendas/nova");
    }

    @PostMapping(value = "/nova", params = "emitir")
    public ModelAndView emitir(Venda venda, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioDoSistema usuarioSistema) {
        validarVenda(venda, result);
        if (result.hasErrors()) {
            return nova(venda);
        }

        venda.setUsuario(usuarioSistema.getUsuario());

        vendaService.emitir(venda);
        attributes.addFlashAttribute("mensagem", "Venda emitida com sucesso");
        return new ModelAndView("redirect:/vendas/nova");
    }

    private void validarVenda(Venda venda, BindingResult result) {
        venda.adicionarItens(tabelaItens.getItens(venda.getUuid()));
        venda.calcularValorTotal();

        vendaValidator.validate(venda, result);
    }

    @PostMapping(value = "/nova", params = "enviarEmail")
    public ModelAndView enviarEmail(Venda venda, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioDoSistema usuarioSistema) {
        validarVenda(venda, result);

        if (result.hasErrors()) {
            return nova(venda);
        }
        venda.setUsuario(usuarioSistema.getUsuario());
        venda = vendaService.salvar(venda);

        mailer.enviar(venda);

        attributes.addFlashAttribute("mensagem", String.format("Venda nº %d salva com sucesso e email enviado com sucesso", venda.getCodigo()));
        return new ModelAndView("redirect:/vendas/nova");
    }


    @PostMapping("/item")
    public ModelAndView adicionarItem(Long codigoCerveja, String uuid) {
        Cerveja cerveja = cervejas.findOne(codigoCerveja);
        tabelaItens.adicionarItem(uuid, cerveja, 1);
        return mvTabelaItensVenda(uuid);

    }

    @PutMapping("/item/{codigoCerveja}")
    public ModelAndView alterarQuantidadeItem(@PathVariable("codigoCerveja") Cerveja cerveja
            , Integer quantidade, String uuid) {
        tabelaItens.alterarQuantidadeItens(uuid, cerveja, quantidade);
        return mvTabelaItensVenda(uuid);
    }

    @DeleteMapping("/item/{uuid}/{codigoCerveja}")
    public ModelAndView excluirItem(@PathVariable("codigoCerveja") Cerveja cerveja, @PathVariable String uuid) {
        tabelaItens.excluirItem(uuid, cerveja);
        return mvTabelaItensVenda(uuid);
    }

    private ModelAndView mvTabelaItensVenda(String uuid) {
        ModelAndView mv = new ModelAndView("venda/TabelaItensVenda");
        mv.addObject("itens", tabelaItens.getItens(uuid));
        mv.addObject("valorTotal", tabelaItens.getValorTotal(uuid));
        return mv;
    }

    @GetMapping
    private ModelAndView pesqusiar(VendaFilter vendaFilter, BindingResult result, @PageableDefault(size = 3) Pageable pageable, HttpServletRequest httpServletRequest) {
        ModelAndView mv = new ModelAndView("venda/PesquisaVendas");
        mv.addObject("todosStatus", StatusVenda.values());
        mv.addObject("tipoPessoa", TipoPessoa.values());
        PageWrapper<Venda> paginaWrapper = new PageWrapper<>(vendas.filtrar(vendaFilter, pageable)
                , httpServletRequest);
        mv.addObject("pagina", paginaWrapper);


        return mv;
    }
}


