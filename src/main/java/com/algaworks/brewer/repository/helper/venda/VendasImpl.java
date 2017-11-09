package com.algaworks.brewer.repository.helper.venda;

import com.algaworks.brewer.models.TipoPessoa;
import com.algaworks.brewer.models.Venda;
import com.algaworks.brewer.repository.filter.VendaFilter;
import com.algaworks.brewer.repository.paginacao.PaginacaoUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class VendasImpl implements VendaQueries {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private PaginacaoUtil paginacaoUtil;

    @Transactional(readOnly = true)
    @Override
    public Page<Venda> filtrar(VendaFilter vendaFilter, Pageable pageable) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Venda.class);
        paginacaoUtil.preparar(criteria, pageable);
        adicionarFiltro(vendaFilter, criteria);
        criteria.addOrder((Order.asc("codigo")));

        return new PageImpl<>(criteria.list(), pageable, total(vendaFilter));

    }

    private Long total(VendaFilter filtro) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Venda.class);
        adicionarFiltro(filtro, criteria);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    private void adicionarFiltro(VendaFilter filtro, Criteria criteria) {
        criteria.createAlias("cliente", "c");

        if (filtro != null) {
            if (!StringUtils.isEmpty(filtro.getCodigo())) {
                criteria.add(Restrictions.eq("codigo", filtro.getCodigo()));
            }

            if (filtro.getStatus() != null) {
                criteria.add(Restrictions.eq("status", filtro.getStatus()));
            }

            if (filtro.getDesde() != null) {
                LocalDateTime desde = LocalDateTime.of(filtro.getDesde(), LocalTime.of(0, 0));
                criteria.add(Restrictions.ge("dataCriacao", desde));
            }

            if (filtro.getAte() != null) {
                LocalDateTime ate = LocalDateTime.of(filtro.getAte(), LocalTime.of(23, 59));
                criteria.add(Restrictions.le("dataCriacao", ate));
            }

            if (filtro.getValorMinimo() != null) {
                criteria.add(Restrictions.ge("valorTotal", filtro.getValorMinimo()));
            }

            if (filtro.getValorMaximo() != null) {
                criteria.add(Restrictions.le("valorTotal", filtro.getValorMaximo()));
            }

            if (!StringUtils.isEmpty(filtro.getNomeCliente())) {
                criteria.add(Restrictions.ilike("c.nome", filtro.getNomeCliente(), MatchMode.ANYWHERE));
            }

            if (!StringUtils.isEmpty(filtro.getCpfOuCnpjCliente())) {
                criteria.add(Restrictions.eq("c.cpfOuCnpj", TipoPessoa.removerFormatacao(filtro.getCpfOuCnpjCliente())));
            }
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Venda buscarComItens(Long codigo) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Venda.class);
        criteria.createAlias("itens", "i", JoinType.LEFT_OUTER_JOIN);
        criteria.add(Restrictions.eq("codigo", codigo));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (Venda) criteria.uniqueResult();
    }
}
