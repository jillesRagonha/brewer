package com.algaworks.brewer.repository.listener;

import com.algaworks.brewer.models.Cerveja;
import com.algaworks.brewer.storage.FotoStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.persistence.PostLoad;

public class CervejaEntityListener {

    @Autowired
    private FotoStorage fotoStorage;

    @PostLoad
    public void postLoad(final Cerveja cerveja) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        cerveja.setUrlFoto(fotoStorage.getUrl(cerveja.getFotoOuMock()));
        cerveja.setUrlThumbnailFoto(fotoStorage.getUrl(FotoStorage.THUMBNAIL_PREFIX + cerveja.getFotoOuMock()));
    }

}
