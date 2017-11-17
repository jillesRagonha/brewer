package com.algaworks.brewer.service.event.venda;

import com.algaworks.brewer.models.Venda;
import org.springframework.context.ApplicationEvent;

public class VendaEvent {

    private Venda venda;

    public VendaEvent(Venda venda) {
        this.venda = venda;
    }

    public Venda getVenda() {
        return venda;
    }
}