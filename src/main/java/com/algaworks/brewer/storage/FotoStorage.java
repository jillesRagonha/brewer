package com.algaworks.brewer.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FotoStorage {

    public String salvarTemporariamente(MultipartFile files[]);

    byte[] recuperarFotoTemporaria(String nome);

    void salvar(String foto);
}
