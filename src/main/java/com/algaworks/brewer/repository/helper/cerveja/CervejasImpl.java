package com.algaworks.brewer.repository.helper.cerveja;

import com.algaworks.brewer.models.Cerveja;
import com.algaworks.brewer.repository.filter.CervejaFilter;
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
import java.util.List;

public class CervejasImpl implements CervejasQueries {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private PaginacaoUtil paginacaoUtil;

    @Override
    @Transactional(readOnly = true)
    public Page<Cerveja> filtrar(CervejaFilter filter, Pageable pageable) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Cerveja.class);

        paginacaoUtil.preparar(criteria,pageable);

        adicionarFiltro(filter, criteria);


        return new PageImpl<Cerveja>(criteria.list(), pageable, total(filter));
    }

    private void adicionarFiltro(CervejaFilter filter, Criteria criteria) {
        if (filter != null) {
            if (!StringUtils.isEmpty(filter.getSku())) {
                criteria.add(Restrictions.eq("sku", filter.getSku()));
            }
            if (!StringUtils.isEmpty(filter.getNome())) {
                criteria.add(Restrictions.ilike("nome", filter.getNome(), MatchMode.ANYWHERE));
            }

            if (isEstiloPresente(filter)) {
                criteria.add(Restrictions.eq("estilo", filter.getEstilo()));
            }

            if (filter.getSabor() != null) {
                criteria.add(Restrictions.eq("sabor", filter.getSabor()));
            }

            if (filter.getOrigem() != null) {
                criteria.add(Restrictions.eq("origem", filter.getOrigem()));
            }

            if (filter.getValorDe() != null) {
                criteria.add(Restrictions.ge("valor", filter.getValorDe()));
            }

            if (filter.getValorAte() != null) {
                criteria.add(Restrictions.le("valor", filter.getValorAte()));
            }
        }
    }

    private Long total(CervejaFilter filtro) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Cerveja.class);
        criteria.setProjection(Projections.rowCount());
        adicionarFiltro(filtro,criteria);
        return (Long) criteria.uniqueResult();
    }

    private boolean isEstiloPresente(CervejaFilter filtro) {
        return filtro.getEstilo() != null && filtro.getEstilo().getCodigo() != null;
    }

}
