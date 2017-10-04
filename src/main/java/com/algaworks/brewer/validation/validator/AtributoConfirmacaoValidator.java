package com.algaworks.brewer.validation.validator;

import com.algaworks.brewer.validation.AtributoConfirmacao;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;

public class AtributoConfirmacaoValidator implements ConstraintValidator<AtributoConfirmacao, Object> {

    private String atributo;
    private String atributoConfirmacao;

    @Override
    public void initialize(AtributoConfirmacao atributoConfirmacao) {
        atributo = atributoConfirmacao.atributo();
        this.atributoConfirmacao = atributoConfirmacao.atributoConfirmacao();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {

        boolean valido = false;
        try {
            Object valorAtributo = BeanUtils.getProperty(o, this.atributo);
            Object valorAtributoConfirmacao = BeanUtils.getProperty(o, this.atributoConfirmacao);

            valido = ambosSaoNull(valorAtributo, valorAtributoConfirmacao) || ambosIguais(valorAtributo,valorAtributoConfirmacao);

        } catch (Exception e) {
            throw  new RuntimeException("Erro recuperando valores dos atributos", e);
        }

        if (!valido) {
            constraintValidatorContext.disableDefaultConstraintViolation();

            String mensagem = constraintValidatorContext.getDefaultConstraintMessageTemplate();
            ConstraintValidatorContext.ConstraintViolationBuilder builder = constraintValidatorContext.buildConstraintViolationWithTemplate(mensagem);
            builder.addPropertyNode(atributoConfirmacao).addConstraintViolation();
        }
        return valido;
    }

    private boolean ambosIguais(Object valorAtributo, Object valorAtributoConfirmacao) {
        return valorAtributo != null && valorAtributo.equals(valorAtributoConfirmacao);
    }

    private boolean ambosSaoNull(Object valorAtributo, Object valorAtributoConfirmacao) {
        return valorAtributo == null && valorAtributoConfirmacao == null;
    }
}
