package com.algaworks.brewer.service;

import com.algaworks.brewer.models.Cerveja;
import com.algaworks.brewer.repository.Cervejas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroCervejaService {
    @Autowired
    private Cervejas cervejas;

    @Transactional
    public void salvar(Cerveja cerveja){
        cervejas.save(cerveja);
    }
}
