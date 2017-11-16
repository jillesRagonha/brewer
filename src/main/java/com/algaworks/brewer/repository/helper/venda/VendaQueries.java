package com.algaworks.brewer.repository.helper.venda;

import com.algaworks.brewer.DTO.VendaMes;
import com.algaworks.brewer.DTO.VendaOrigem;
import com.algaworks.brewer.models.Venda;
import com.algaworks.brewer.repository.filter.VendaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface VendaQueries  {

    public Page<Venda> filtrar(VendaFilter vendaFilter, Pageable pageable);

    public Venda buscarComItens(Long codigo);

    public BigDecimal valorTotalNoAno();

    public BigDecimal valorTotalNoMes();

    public BigDecimal valorTicketMedio();

    public List<VendaMes> totalPorMes();

    public List<VendaOrigem> totalPorOrigem();

}
