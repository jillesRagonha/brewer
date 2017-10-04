package com.algaworks.brewer.repository;

import com.algaworks.brewer.models.Cidade;
import com.algaworks.brewer.models.Estado;
import com.algaworks.brewer.repository.helper.cidade.CidadesQueries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface Cidades extends JpaRepository<Cidade, Long>, CidadesQueries {
    public List<Cidade> findByEstadoCodigo(Long codigo);

    public Optional<Cidade> findByNomeAndEstado(String nome, Estado estado);
}
