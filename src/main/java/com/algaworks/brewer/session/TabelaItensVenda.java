package com.algaworks.brewer.session;

import com.algaworks.brewer.models.Cerveja;
import com.algaworks.brewer.models.ItemVenda;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;


class TabelaItensVenda {

    private String uuid;
    private List<ItemVenda> itens = new ArrayList<>();


    public TabelaItensVenda(String uuid) {
        this.uuid = uuid;
    }

    public void alterarQuantidadeItens(Cerveja cerveja, Integer quantidade) {
        ItemVenda itemVenda = buscarItemPorCerveja(cerveja).get();
        itemVenda.setQuantidade(quantidade);
    }

    public BigDecimal getValorTotal() {
        return itens.stream()
                .map(ItemVenda::getValorTotal)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public void excluirItem(Cerveja cerveja) {
        int indice = IntStream.range(0, itens.size())
                .filter(i -> itens.get(i).getCerveja().equals(cerveja))
                .findAny().getAsInt();
        itens.remove(indice);
    }

    public void adicionarItem(Cerveja cerveja, Integer quantidade) {
        Optional<ItemVenda> itemVendaOptional = itens.stream()
                .filter(i -> i.getCerveja().equals(cerveja))
                .findAny();

        ItemVenda itemVenda = null;
        if (itemVendaOptional.isPresent()) {
            itemVenda = itemVendaOptional.get();
            itemVenda.setQuantidade(itemVenda.getQuantidade() + quantidade);

        } else {
            itemVenda = new ItemVenda();
            itemVenda.setCerveja(cerveja);
            itemVenda.setQuantidade(quantidade);
            itemVenda.setValorUnitario(cerveja.getValor());
            itens.add(0, itemVenda);
        }


    }

    public int total() {
        return itens.size();
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }

    private Optional<ItemVenda> buscarItemPorCerveja(Cerveja cerveja) {
        return itens.stream()
                .filter(i -> i.getCerveja().equals(cerveja))
                .findAny();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TabelaItensVenda that = (TabelaItensVenda) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
