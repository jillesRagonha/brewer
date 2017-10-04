package com.algaworks.brewer.models;

import com.algaworks.brewer.models.validation.group.CnpjGroup;
import com.algaworks.brewer.models.validation.group.CpfGroup;
import org.hibernate.validator.constraints.br.CNPJ;

public enum TipoPessoa {
    FISICA("Física", "CPF", "000.000.000-00", CpfGroup.class) {
        @Override
        public String formatar(String cpfOuCnpj) {
            return  cpfOuCnpj.replaceAll("(\\d{3})(\\d{3})(\\d{3})","$1.$2.$3-");

        }
    },
    JURIDICA("Jurídica", "CNPJ", "00.000.000/0000-00", CnpjGroup.class) {
        @Override
        public String formatar(String cpfOuCnpj) {
            return  cpfOuCnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})","$1.$2.$3/$4-");
        }
    };

    private String descricao, documento, mascara;
    private Class<?> grupo;


    TipoPessoa(String descricao, String documento, String mascara, Class<?> grupo) {
        this.descricao = descricao;
        this.documento = documento;
        this.mascara = mascara;
        this.grupo = grupo;
    }



    public Class<?> getGrupo() {
        return grupo;
    }


    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getMascara() {
        return mascara;
    }

    public void setMascara(String mascara) {
        this.mascara = mascara;
    }

    public static String removerFormatacao(String cpfOuCnpj){
        return cpfOuCnpj.replaceAll("\\.|-|/", "");
    }

    public abstract String formatar(String cpfOuCnpj);
}
