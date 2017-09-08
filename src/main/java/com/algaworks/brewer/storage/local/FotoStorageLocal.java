package com.algaworks.brewer.storage.local;

import com.algaworks.brewer.storage.FotoStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static java.nio.file.FileSystems.*;

public class FotoStorageLocal implements FotoStorage {
    private static final Logger logger = LoggerFactory.getLogger(FotoStorageLocal.class);

    private Path local;
    private Path localTemporario;
    private String novoNome = null;

    public FotoStorageLocal() {
        this(getDefault().getPath(System.getProperty("user.home"), "brewerFotos"));

    }

    public FotoStorageLocal(Path path) {
        this.local = path;
        criarPastas();
    }

    private void criarPastas() {
        try {
            Files.createDirectories(this.local);
            this.localTemporario = getDefault().getPath(this.local.toString(), "temp");
            Files.createDirectories(this.localTemporario);

            if (logger.isDebugEnabled()) {
                logger.debug("Pastas criada  para salvar as fotos");
                logger.debug("Pasta default: " + this.local.toAbsolutePath());
                logger.debug("Pasta temporária: " + this.localTemporario.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro criando a pasta para salvar as fotos das cervejas", e);
        }
    }

    @Override
    public String salvarTemporariamente(MultipartFile[] files) {

        if (files != null && files.length > 0) {
            MultipartFile arquivo = files[0];
            novoNome = renomearArquivo(arquivo.getOriginalFilename());
            try {
                arquivo.transferTo(new File(this.localTemporario.toAbsolutePath().toString() + getDefault().getSeparator() + novoNome));
            } catch (IOException e) {
                throw new RuntimeException("Erro salvando foto na pasta temporarioa: " + e);
            }
        }
        return novoNome;

    }

    private String renomearArquivo(String nomeArquivo) {
        String novoNome = UUID.randomUUID().toString() + "_" + nomeArquivo;
        if (logger.isDebugEnabled()) {
            logger.debug("Nome Original: %s" + nomeArquivo + "Novo nome do arquivo é: %s" + novoNome);
        }
        return novoNome;
    }

    @Override
    public byte[] recuperarFotoTemporaria(String nome) {
        try {
            return Files.readAllBytes(this.localTemporario.resolve(nome));
        } catch (IOException e) {
            throw new RuntimeException("Erro lendo a foto temporaria: " + e);
        }
    }
}
