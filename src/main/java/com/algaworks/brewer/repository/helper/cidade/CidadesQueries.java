package com.algaworks.brewer.repository.helper.cidade;

import com.algaworks.brewer.models.Cidade;
import com.algaworks.brewer.repository.filter.CidadeFilter;
import com.algaworks.brewer.repository.filter.ClienteFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CidadesQueries {
    public Page<Cidade> filtrar(CidadeFilter cidadeFilter, Pageable pageable);
}
