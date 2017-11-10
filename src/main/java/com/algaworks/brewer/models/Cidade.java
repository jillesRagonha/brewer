package com.algaworks.brewer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "cidade")
public class Cidade implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_estado")
    @JsonIgnore
    private Estado  estado;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    @NotBlank(message = "Preencha o nome da cidade")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }



    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cidade cidade = (Cidade) o;
        return Objects.equals(codigo, cidade.codigo) &&
                Objects.equals(nome, cidade.nome) &&
                Objects.equals(estado, cidade.estado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, nome, estado);
    }

    public boolean temEstado() {
        return estado != null;
    }

    public boolean isNova() {
        return codigo == null;
    }
}
