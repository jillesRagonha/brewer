package com.algaworks.brewer.service;

import com.algaworks.brewer.models.Cidade;
import com.algaworks.brewer.repository.Cidades;
import com.algaworks.brewer.service.exception.NomeCidadeJaCadastradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CadastroCidadeService {

    @Autowired
    private Cidades cidades;

    @Transactional
    public void salvar(Cidade cidade) {
        Optional<Cidade> cidadeJaExistente = cidades.findByNomeAndEstado(cidade.getNome(), cidade.getEstado());
        if (cidadeJaExistente.isPresent()) {
            throw new NomeCidadeJaCadastradoException("Cidade já existe no banco de dados");
        }
        cidades.save(cidade);
    }
}
