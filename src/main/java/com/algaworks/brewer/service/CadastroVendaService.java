package com.algaworks.brewer.service;

import com.algaworks.brewer.models.ItemVenda;
import com.algaworks.brewer.models.StatusVenda;
import com.algaworks.brewer.models.Venda;
import com.algaworks.brewer.repository.Vendas;
import com.algaworks.brewer.service.event.venda.VendaEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class CadastroVendaService {

    @Autowired
    private Vendas vendas;

    @Autowired
    ApplicationEventPublisher publisher;

    @Transactional
    public Venda salvar(Venda venda) {
        if (venda.isSalvarProibido()) {
            throw new RuntimeException("Usuário tentando salvar venda proibida");
        }

        if (venda.isNova()) {
            venda.setDataCriacao(LocalDateTime.now());
        }else {
            Venda vendaExistente = vendas.findOne(venda.getCodigo());
            venda.setDataCriacao(vendaExistente.getDataCriacao());

        }

        if (venda.getDataEntrega() != null) {
            venda.setDataHoraEntrega(LocalDateTime.of(venda.getDataEntrega(), venda.getHorarioEntrega() !=null?
                    venda.getHorarioEntrega() : LocalTime.NOON));
        }

        return vendas.saveAndFlush(venda);

    }

    @Transactional
    public void emitir(Venda venda) {
        venda.setStatus(StatusVenda.EMITIDA);
        salvar(venda);

        publisher.publishEvent(new VendaEvent(venda));
    }

    @PreAuthorize("#venda.usuario == principal.username or hasRole('CANCELAR_VENDA')") //verifica se o msm usuario que criou a venda eh o q esta cvancelando
    @Transactional
    public void cancelar(Venda venda) {
        Venda vendaExistente = vendas.findOne(venda.getCodigo());
        vendaExistente.setStatus(StatusVenda.CANCELADA);
        vendas.save(vendaExistente);
    }
}
