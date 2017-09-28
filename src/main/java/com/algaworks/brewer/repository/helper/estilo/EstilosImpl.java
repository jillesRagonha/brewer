package com.algaworks.brewer.repository.helper.estilo;

import com.algaworks.brewer.models.Estilo;
import com.algaworks.brewer.repository.filter.EstiloFilter;
import com.algaworks.brewer.repository.paginacao.PaginacaoUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class EstilosImpl implements EstilosQueries {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private PaginacaoUtil paginacaoUtil;

    @Override
    @Transactional(readOnly = true)
    public Page<Estilo> filtrar(EstiloFilter filter, Pageable pageable) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Estilo.class);
        paginacaoUtil.preparar(criteria, pageable);
        adicionarFiltro(filter, criteria);

        return new PageImpl<Estilo>(criteria.list(), pageable, total(filter));
    }

    private long total(EstiloFilter filter) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Estilo.class);
        criteria.setProjection(Projections.rowCount());
        adicionarFiltro(filter, criteria);
        return (Long) criteria.uniqueResult();
    }

    private void adicionarFiltro(EstiloFilter filter, Criteria criteria) {
        if (filter != null) {
            if (!StringUtils.isEmpty(filter.getNome())) {
                criteria.add(Restrictions.ilike("nome", filter.getNome(), MatchMode.ANYWHERE));
            }
        }
    }
}
