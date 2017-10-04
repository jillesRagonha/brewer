package com.algaworks.brewer.models.validation;

import com.algaworks.brewer.models.Cliente;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

public class ClienteGroupSequenceProvider implements DefaultGroupSequenceProvider<Cliente> {

    @Override
    public List<Class<?>> getValidationGroups(Cliente cliente) {
        List<Class<?>> grupos = new ArrayList<>();
        grupos.add(Cliente.class);
        if (isTipoPessoaSelecionada(cliente)) {
            grupos.add(cliente.getTipoPessoa().getGrupo());
        }
        return grupos;
    }

    private boolean isTipoPessoaSelecionada(Cliente cliente) {
        return cliente != null && cliente.getTipoPessoa() != null;
    }
}
