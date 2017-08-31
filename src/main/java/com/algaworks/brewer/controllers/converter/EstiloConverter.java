package com.algaworks.brewer.controllers.converter;

import com.algaworks.brewer.models.Estilo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

public class EstiloConverter implements Converter<String, Estilo> {

    @Override
    public Estilo convert(String source) {
        if (!StringUtils.isEmpty(source)) {
            Estilo estilo = new Estilo();
            estilo.setCodigo(Long.valueOf(source));
            return estilo;
        }
        return null;
    }
}
