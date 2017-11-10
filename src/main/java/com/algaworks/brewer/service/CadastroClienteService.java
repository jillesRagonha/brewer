package com.algaworks.brewer.service;

import com.algaworks.brewer.models.Cliente;
import com.algaworks.brewer.repository.Clientes;
import com.algaworks.brewer.service.exception.CpfClienteJaCadastradoException;
import com.algaworks.brewer.service.exception.ImpossivelExcluirEntidadeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.Optional;

@Service
public class CadastroClienteService {

    @Autowired
    private Clientes clientes;

    @Transactional
    public void salvar(Cliente cliente) {
        Optional<Cliente> clienteExistente = clientes.findByCpfOuCnpj(cliente.getCpfOuCnpjSemFormatacao());
        if (clienteExistente.isPresent() && cliente.isNovo()) {
            throw new CpfClienteJaCadastradoException("CPF/CNPJ já cadastrado");
        }
        clientes.save(cliente);
    }

    @Transactional
    public void excluir(Cliente cliente) {
        try {
            clientes.delete(cliente);
            clientes.flush();
        } catch (PersistenceException e) {
            throw new ImpossivelExcluirEntidadeException("Não é possível excluir esse cliente, provavelmente ele está relacionado a alguma venda");
        }
    }
}
