package com.algaworks.brewer.repository.helper.cliente;

import com.algaworks.brewer.models.Cliente;
import com.algaworks.brewer.models.Endereco;
import com.algaworks.brewer.repository.filter.ClienteFilter;
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

public class ClientesImpl implements ClientesQueries {
    @PersistenceContext
    private EntityManager manager;
    @Autowired
    private PaginacaoUtil paginacaoUtil;

    @Transactional(readOnly = true)
    @Override
    public Page<Cliente> filtrar(ClienteFilter clienteFilter, Pageable pageable) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Cliente.class);
        paginacaoUtil.preparar(criteria, pageable);
        adicionarFiltro(clienteFilter, criteria);
        criteria.createAlias("endereco.cidade", "c", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("c.estado", "e", JoinType.LEFT_OUTER_JOIN);

        return new PageImpl<Cliente>(criteria.list(), pageable, total(clienteFilter));

    }

    private Long total(ClienteFilter clienteFilter) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Cliente.class);
        criteria.setProjection(Projections.rowCount());
        adicionarFiltro(clienteFilter, criteria);
        return (Long) criteria.uniqueResult();

    }

    private void adicionarFiltro(ClienteFilter clienteFilter, Criteria criteria) {
        if (clienteFilter != null) {
            if (!StringUtils.isEmpty(clienteFilter.getNome())) {
                criteria.add(Restrictions.ilike("nome", clienteFilter.getNome(), MatchMode.ANYWHERE));
            }
            if (!StringUtils.isEmpty(clienteFilter.getCpfOuCnpj())) {
                criteria.add(Restrictions.ilike("cpfOuCnpj", clienteFilter.getCpfOuCnpjSemFormatacao(), MatchMode.ANYWHERE));
            }
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Cliente encontrarCliente(Long codigo) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Cliente.class);
        criteria.createAlias("endereco.cidade", "c", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("c.estado", "e", JoinType.LEFT_OUTER_JOIN);
        criteria.add(Restrictions.eq("codigo", codigo));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return (Cliente) criteria.uniqueResult();
    }


}
