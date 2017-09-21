package com.algaworks.brewer.models;


import com.algaworks.brewer.validation.SKU;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;


@Entity
@Table(name = "cerveja")
public class Cerveja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @SKU
    @NotBlank(message = "Campo SKU precisa ser preenchido")
    private String sku;

    @NotBlank(message = "Preencha o campo nome")
    private String nome;

    @Size(min = 1, max = 100, message = "O campo descrição deve conter de 0 à 100 caracteres")
    private String descricao;

    @NotNull(message = "Valor do produto está vazio, preencha corretamente")
    @DecimalMax(value = "999999.99", message = "O valor está muito alto, confira novamente")
    @DecimalMin(value = "0.01", message = "O valor está muito baixo, confira novamente")
    private BigDecimal valor;

    @NotNull(message = "O campo teor Alcóolico é obrigatório")
    @DecimalMax(value = "100.0", message = "O teor alcóolico está muito alto")
    @DecimalMin(value = "0.01")
    @Column(name = "teor_alcoolico")
    private BigDecimal teorAlcoolico;

    @Max(value = 9999, message = "A quantidade de produto em estoque é muito alta")
    @Column(name = "quantidade_estoque")
    private int qtdeEstoque;

    @NotNull(message = "Selecione a Origem do produto")
    @Enumerated(EnumType.STRING)
    private Origem origem;

    @NotNull(message = "Selecione o sabor")
    @Enumerated(EnumType.STRING)
    private Sabor sabor;

    @NotNull(message = "Entre com o valor da comissão")
    @DecimalMax(value = "100.0", message = "Comissão acima de 100%, tem certeza?")
    private BigDecimal comissao;

    @NotNull(message = "Selecione um estilo")
    @ManyToOne
    @JoinColumn(name = "codigo_estilo")
    private Estilo estilo;

    private String foto;

    @Column(name = "content_type")
    private String contentType;

    public String getFoto() {

        return foto;
    }

    public String getFotoOuMock() {
        return !StringUtils.isEmpty(foto) ? foto : "cerveja-mock.png";
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getTeorAlcoolico() {
        return teorAlcoolico;
    }

    public void setTeorAlcoolico(BigDecimal teorAlcoolico) {
        this.teorAlcoolico = teorAlcoolico;
    }


    public int getQtdeEstoque() {
        return qtdeEstoque;
    }

    public void setQtdeEstoque(int qtdeEstoque) {
        this.qtdeEstoque = qtdeEstoque;
    }

    public Origem getOrigem() {
        return origem;
    }

    public void setOrigem(Origem origem) {
        this.origem = origem;
    }

    public Sabor getSabor() {
        return sabor;
    }

    public void setSabor(Sabor sabor) {
        this.sabor = sabor;
    }

    public Estilo getEstilo() {
        return estilo;
    }

    public void setEstilo(Estilo estilo) {
        this.estilo = estilo;
    }

    public BigDecimal getComissao() {
        return comissao;
    }

    public void setComissao(BigDecimal comissao) {
        this.comissao = comissao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cerveja cerveja = (Cerveja) o;

        return codigo != null ? codigo.equals(cerveja.codigo) : cerveja.codigo == null;
    }

    @Override
    public int hashCode() {
        return codigo != null ? codigo.hashCode() : 0;
    }

    @PrePersist
    @PreUpdate
    private void prePersistUpdate() {
        sku = sku.toUpperCase();
    }
}
