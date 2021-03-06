package com.algaworks.brewer.service.event.venda;

import com.algaworks.brewer.models.Cerveja;
import com.algaworks.brewer.models.ItemVenda;
import com.algaworks.brewer.repository.Cervejas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class VendaListener {

    @Autowired
    private Cervejas cervejas;

    @EventListener
    public void vendaEmitida(VendaEvent vendaEvent) {
        for (ItemVenda item : vendaEvent.getVenda().getItens()) {
            Cerveja cerveja = cervejas.findOne(item.getCerveja().getCodigo());
            cerveja.setQtdeEstoque(cerveja.getQtdeEstoque() - item.getQuantidade());
            cervejas.save(cerveja);
        }
    }
}
