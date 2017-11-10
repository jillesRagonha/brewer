package com.algaworks.brewer.repository.helper.cidade;

import com.algaworks.brewer.models.Cidade;
import com.algaworks.brewer.repository.filter.CidadeFilter;
import com.algaworks.brewer.repository.paginacao.PaginacaoUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CidadesImpl implements CidadesQueries {

    @PersistenceContext
    private EntityManager manager;
    @Autowired
    private PaginacaoUtil paginacaoUtil;

    @Transactional(readOnly = true)
    @Override
    public Page<Cidade> filtrar(CidadeFilter cidadeFilter, Pageable pageable) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Cidade.class);
        paginacaoUtil.preparar(criteria, pageable);
        adicionarFiltro(cidadeFilter, criteria);
        criteria.createAlias("estado", "e");


        return new PageImpl<Cidade>(criteria.list(), pageable, total(cidadeFilter));

    }

    private Long total(CidadeFilter cidadeFilter) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Cidade.class);
        criteria.setProjection(Projections.rowCount());
        adicionarFiltro(cidadeFilter, criteria);
        return (Long) criteria.uniqueResult();
    }

    private void adicionarFiltro(CidadeFilter cidadeFilter, Criteria criteria) {
        if (cidadeFilter != null) {
            if (!StringUtils.isEmpty(cidadeFilter.getNome())) {
                criteria.add(Restrictions.ilike("nome", cidadeFilter.getNome(), MatchMode.ANYWHERE));
            }
            if (!StringUtils.isEmpty(cidadeFilter.getEstado())) {
                criteria.add(Restrictions.eq("estado", cidadeFilter.getEstado()));

            }

        }
    }

    @Override
    @Transactional(readOnly = true)
    public Cidade buscarComEstados(Long codigo) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Cidade.class);
        criteria.createAlias("estado", "e", JoinType.LEFT_OUTER_JOIN);
        criteria.add(Restrictions.eq("codigo", codigo));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return (Cidade) criteria.uniqueResult();
    }
}
