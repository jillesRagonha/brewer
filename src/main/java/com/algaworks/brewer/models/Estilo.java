package com.algaworks.brewer.models;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "estilo")
public class Estilo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Size(max = 35, min = 1, message = "O campo precisa ter no máximo 35 caracteres e no mínimo 1")
    private String nome;

    @OneToMany(mappedBy = "estilo")
    private List<Cerveja> cervejas;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estilo estilo = (Estilo) o;
        return Objects.equals(codigo, estilo.codigo) &&
                Objects.equals(nome, estilo.nome) &&
                Objects.equals(cervejas, estilo.cervejas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, nome, cervejas);
    }

    public boolean isNovo() {
        return codigo != null;
    }
}
