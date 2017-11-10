package com.algaworks.brewer.service;

import com.algaworks.brewer.models.Estilo;
import com.algaworks.brewer.repository.Estilos;
import com.algaworks.brewer.service.exception.ImpossivelExcluirEntidadeException;
import com.algaworks.brewer.service.exception.NomeEstiloJaCadastradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.Optional;

@Service
public class CadastroEstiloService {

    @Autowired
    private Estilos estilos;

    @Transactional
    public Estilo salvar(Estilo estilo) {
        Optional<Estilo> estiloOptional = estilos.findByNomeIgnoreCase(estilo.getNome());
        if (estiloOptional.isPresent()) {
            throw new NomeEstiloJaCadastradoException("Nome do estilo já cadastrado");
        }

        return estilos.saveAndFlush(estilo);
    }

    @Transactional
    public void excluir(Estilo estilo) {
        try {
            estilos.delete(estilo);
            estilos.flush();
        } catch (PersistenceException e) {
            throw new ImpossivelExcluirEntidadeException("Erro ao excluir a entidade Estilo");
        }
    }
}
