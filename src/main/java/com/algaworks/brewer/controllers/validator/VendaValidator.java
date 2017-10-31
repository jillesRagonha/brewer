package com.algaworks.brewer.controllers.validator;

import com.algaworks.brewer.models.Venda;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

@Component
public class VendaValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Venda.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "cliente.codigo", "", "Selecione um Cliente na pesquisa Rápida");
        Venda venda = (Venda)o;
        validarSeInformouApenasHorarioEntrega(errors, venda);
        validarSeAdicionouItens(errors, venda);
            validarValorTotalNegativo(errors, venda);

    }

    private void validarValorTotalNegativo(Errors errors, Venda venda) {
        if (venda.getValorTotal().compareTo(BigDecimal.ZERO) < 0) {
            errors.reject("","Valor total não pode ser negativo");
        }
    }

    private void validarSeAdicionouItens(Errors errors, Venda venda) {
        if (venda.getItens().isEmpty()) {
            errors.reject("", "Adicione pelo menos um item na venda");
        }
    }

    private void validarSeInformouApenasHorarioEntrega(Errors errors, Venda venda) {
        if (venda.getHorarioEntrega() != null && venda.getDataEntrega() == null) {
            errors.rejectValue("dataEntrega","","Informe uma data de Entrega para o horário escolhido");
        }
    }
}
