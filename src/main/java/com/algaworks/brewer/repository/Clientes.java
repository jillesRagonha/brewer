package com.algaworks.brewer.repository;

import com.algaworks.brewer.models.Cliente;
import com.algaworks.brewer.repository.filter.ClienteFilter;
import com.algaworks.brewer.repository.helper.cliente.ClientesQueries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.awt.print.Pageable;
import java.util.Optional;

@EnableJpaRepositories
public interface Clientes extends JpaRepository<Cliente, Long>, ClientesQueries {

    public Optional<Cliente> findByCpfOuCnpj(String cpfOuCnpj);

}
