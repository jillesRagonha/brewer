package com.algaworks.brewer.controllers.converter;

import com.algaworks.brewer.models.Cidade;
import com.algaworks.brewer.models.Estado;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

public class EstadoConverter implements Converter<String, Estado> {

    @Override
    public Estado convert(String source) {
        if (!StringUtils.isEmpty(source)) {
            Estado estado = new Estado();
            estado.setCodigo(Long.valueOf(source));
            return estado;
        }
        return null;
    }
}
