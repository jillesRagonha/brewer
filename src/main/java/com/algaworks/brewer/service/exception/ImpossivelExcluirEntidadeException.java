package com.algaworks.brewer.service.exception;

public class ImpossivelExcluirEntidadeException extends RuntimeException {
    private static final Long serialVersionUID= 1L;

    public ImpossivelExcluirEntidadeException(String s) {
        super(s);
    }
}
