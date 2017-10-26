package com.algaworks.brewer.controllers;

import com.algaworks.brewer.models.Cerveja;
import com.algaworks.brewer.models.Venda;
import com.algaworks.brewer.repository.Cervejas;
import com.algaworks.brewer.security.UsuarioDoSistema;
import com.algaworks.brewer.service.CadastroVendaService;
import com.algaworks.brewer.session.TabelaItensSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/vendas")
public class VendasController {


    @Autowired
    private Cervejas cervejas;
    @Autowired
    private TabelaItensSession tabelaItens;
    @Autowired
    private CadastroVendaService vendaService;

    @GetMapping("/nova")
    public ModelAndView nova(Venda venda) {
        ModelAndView mv = new ModelAndView("venda/CadastroVenda");
        venda.setUuid(UUID.randomUUID().toString());
        return mv;
    }

    @PostMapping("/nova")
    public ModelAndView salvar(Venda venda, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioDoSistema usuarioSistema) {
        venda.setUsuario(usuarioSistema.getUsuario());
        venda.adicionarItens(tabelaItens.getItens(venda.getUuid()));

        vendaService.salvar(venda);
        attributes.addFlashAttribute("mensagem", "Venda salva com sucesso");
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
}
