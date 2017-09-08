package com.algaworks.brewer.controllers;

import com.algaworks.brewer.DTO.FotoDTO;
import com.algaworks.brewer.storage.FotoStorage;
import com.algaworks.brewer.storage.FotoStorageRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/fotos")
public class FotosController {

    @Autowired
    private FotoStorage fotoStorage;

    @PostMapping
    public DeferredResult<FotoDTO> upload(@RequestParam("files[]") MultipartFile[] files){
        DeferredResult<FotoDTO> deferredResult = new DeferredResult<>();

        Thread thread = new Thread(new FotoStorageRunnable(files,deferredResult, fotoStorage));
        thread.start();

        return deferredResult;
    }

    @GetMapping("/temp/{nome:.*}")
    public byte[] recuperarFotoTemporaria(@PathVariable String nome) {
        return fotoStorage.recuperarFotoTemporaria(nome);
    }

}
