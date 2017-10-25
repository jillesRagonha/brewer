package com.algaworks.brewer.session;

import com.algaworks.brewer.models.Cerveja;
import com.algaworks.brewer.models.ItemVenda;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SessionScope
@Component
public class TabelaItensSession {
    private Set<TabelaItensVenda> tabelas = new HashSet<>();

    public void adicionarItem(String uuid, Cerveja cerveja, int quantidade) {
        TabelaItensVenda tabelaItensVenda = buscarTabelaPorUUID(uuid);

        tabelaItensVenda.adicionarItem(cerveja,quantidade);
        tabelas.add(tabelaItensVenda);
    }

    public void alterarQuantidadeItens(String uuid, Cerveja cerveja, Integer quantidade) {
        TabelaItensVenda tabelaItensVenda = buscarTabelaPorUUID(uuid);
        tabelaItensVenda.alterarQuantidadeItens(cerveja,quantidade);

    }

    public void excluirItem(String uuid, Cerveja cerveja) {
        TabelaItensVenda tabelaItensVenda = buscarTabelaPorUUID(uuid);
        tabelaItensVenda.excluirItem(cerveja);
    }

    public List<ItemVenda> getItens(String uuid) {
        return buscarTabelaPorUUID(uuid).getItens();
    }

    private TabelaItensVenda buscarTabelaPorUUID(String uuid) {
        return tabelas.stream()
                .filter(t -> t.getUuid().equals(uuid))
                .findAny()
                .orElse(new TabelaItensVenda(uuid));
    }
}
