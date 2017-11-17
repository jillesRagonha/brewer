package com.algaworks.brewer.storage;

import com.algaworks.brewer.DTO.FotoDTO;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

public class FotoStorageRunnable implements Runnable {


    private MultipartFile[] files;
    private DeferredResult<FotoDTO> deferredResult;
    private FotoStorage fotoStorage;


    public FotoStorageRunnable(MultipartFile[] files, DeferredResult<FotoDTO> deferredResult, FotoStorage fotoStorage) {
        this.files = files;
        this.deferredResult = deferredResult;
        this.fotoStorage = fotoStorage;
    }

    @Override
    public void run() {
        System.out.println(">>>>>FILES<<<<<< " + files[0].getSize());
        String nomeFoto = this.fotoStorage.salvar(files);
        String contentType = files[0].getContentType();
        deferredResult.setResult(new FotoDTO(nomeFoto,contentType, fotoStorage.getUrl(nomeFoto)));
    }
}
