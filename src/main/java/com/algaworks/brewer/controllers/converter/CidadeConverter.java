package com.algaworks.brewer.controllers.converter;

import com.algaworks.brewer.models.Cidade;
import com.algaworks.brewer.models.Estilo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

public class CidadeConverter implements Converter<String, Cidade> {

    @Override
    public Cidade convert(String source) {
        if (!StringUtils.isEmpty(source)) {
            Cidade cidade = new Cidade();
            cidade.setCodigo(Long.valueOf(source));
            return cidade;
        }
        return null;
    }
}
