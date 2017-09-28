package com.algaworks.brewer.repository;

import com.algaworks.brewer.models.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface Cidades extends JpaRepository<Cidade, Long> {
    public List<Cidade> findByEstadoCodigo(Long codigo);
}
