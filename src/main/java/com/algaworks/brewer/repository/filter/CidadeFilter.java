package com.algaworks.brewer.repository.filter;

import com.algaworks.brewer.models.Estado;

public class CidadeFilter {

    private String nome;
    private Estado estado;

    public String getNome() {
        return nome;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
