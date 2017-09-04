package com.algaworks.brewer.controllers.handler;

import com.algaworks.brewer.service.exception.NomeEstiloJaCadastradoException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerAdviceExceptionHandler {
    @ExceptionHandler(NomeEstiloJaCadastradoException.class)
    public ResponseEntity<String> handleNomeCadastradoException(NomeEstiloJaCadastradoException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
