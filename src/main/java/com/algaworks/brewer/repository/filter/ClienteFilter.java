package com.algaworks.brewer.repository.filter;

import com.algaworks.brewer.models.TipoPessoa;

public class ClienteFilter {

    private String nome;
    private String cpfOuCnpj;

    public String getCpfOuCnpj() {
        return cpfOuCnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpfOuCnpj(String cpfOuCnpj) {
        this.cpfOuCnpj = cpfOuCnpj;
    }

    public String getCpfOuCnpjSemFormatacao(){
        return TipoPessoa.removerFormatacao(this.cpfOuCnpj);
    }
}
