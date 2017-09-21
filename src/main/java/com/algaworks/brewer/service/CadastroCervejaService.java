package com.algaworks.brewer.service;

import com.algaworks.brewer.models.Cerveja;
import com.algaworks.brewer.repository.Cervejas;
import com.algaworks.brewer.service.event.cerveja.CervejaSalvaEvento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroCervejaService {
    @Autowired
    private Cervejas cervejas;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Transactional
    public void salvar(Cerveja cerveja){
        cervejas.save(cerveja);

        publisher.publishEvent(new CervejaSalvaEvento(cerveja));

    }
}
