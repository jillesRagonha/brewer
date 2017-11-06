package com.algaworks.brewer.repository;

import com.algaworks.brewer.models.Venda;
import com.algaworks.brewer.repository.helper.venda.VendaQueries;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Vendas extends JpaRepository<Venda, Long>, VendaQueries {
}
