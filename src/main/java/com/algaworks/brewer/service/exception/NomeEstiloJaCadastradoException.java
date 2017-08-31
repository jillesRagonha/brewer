package com.algaworks.brewer.service.exception;

public class NomeEstiloJaCadastradoException extends RuntimeException {
    private final static long serialVersionUID= 1L;

    public NomeEstiloJaCadastradoException(String mensagem) {
        super(mensagem);
    }
}
