package com.algaworks.brewer.repository.helper.venda;

import com.algaworks.brewer.models.Venda;
import com.algaworks.brewer.repository.filter.VendaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VendaQueries  {

    public Page<Venda> filtrar(VendaFilter vendaFilter, Pageable pageable);
}
