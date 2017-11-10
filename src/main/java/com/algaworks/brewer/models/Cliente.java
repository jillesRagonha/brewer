package com.algaworks.brewer.models;

import com.algaworks.brewer.models.validation.ClienteGroupSequenceProvider;
import com.algaworks.brewer.models.validation.group.CnpjGroup;
import com.algaworks.brewer.models.validation.group.CpfGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.hibernate.validator.group.GroupSequenceProvider;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "cliente")
@GroupSequenceProvider(ClienteGroupSequenceProvider.class)
@DynamicUpdate
public class Cliente implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @NotBlank(message = "Nome é Obrigatório")
    private String nome;

    @NotNull(message = "Escolha o tipo de pessoa")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pessoa")
    private TipoPessoa tipoPessoa;

    @NotBlank(message = "CPF ou CNPJ obrigatório")
    @CPF(message = "Número de CPF inválido", groups = CpfGroup.class)
    @CNPJ(message = "Número de CNPJ Inválido", groups = CnpjGroup.class)
    @Column(name = "cpf_cnpj")
    private String cpfOuCnpj;

    private String telefone;

    @Email(message = "E-mail inválido")
    private String email;

    @JsonIgnore
    @Embedded
    private Endereco endereco;

    //tira formatacao de pontos no cpf ou cnpj
    @PrePersist @PreUpdate
    private void prePersistPreUpdate(){
       this.cpfOuCnpj = TipoPessoa.removerFormatacao(this.cpfOuCnpj);

    }

    @PostLoad
    private void postLoad() {
        this.cpfOuCnpj = tipoPessoa.formatar(this.cpfOuCnpj);
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoPessoa getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoa tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public String getCpfOuCnpj() {
        return cpfOuCnpj;
    }

    public void setCpfOuCnpj(String cpfOuCnpj) {
        this.cpfOuCnpj = cpfOuCnpj;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getCpfOuCnpjSemFormatacao(){
        return TipoPessoa.removerFormatacao(this.cpfOuCnpj);

    }

    public boolean isNovo() {
        return codigo == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(codigo, cliente.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
