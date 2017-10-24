package com.algaworks.brewer.session;

import com.algaworks.brewer.models.Cerveja;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class TabelaItensVendaTest {

    private TabelaItensVenda tabelaItensVenda;

    @Before
    public void setUp(){
        this.tabelaItensVenda = new TabelaItensVenda();
    }

    @Test
    public void deveCalcularValorTotalSemItens() throws Exception {
        assertEquals(BigDecimal.ZERO, tabelaItensVenda.getValorTotal());
    }

    @Test
    public void deveCalcularValorTotalComUmItem() throws Exception {
        Cerveja cerveja = new Cerveja();
        BigDecimal valor = new BigDecimal("8.90");
        cerveja.setValor(valor);
        tabelaItensVenda.adicionarItem(cerveja, 1);
        assertEquals(valor, tabelaItensVenda.getValorTotal());

    }
    @Test
    public void deveCalcularValorTotalComVariosItens() throws Exception {
        Cerveja cerveja1 = new Cerveja();
        cerveja1.setCodigo(1L);
        BigDecimal valor1 = new BigDecimal("8.90");

        Cerveja cerveja2 = new Cerveja();
        BigDecimal valor2 = new BigDecimal("1.90");
        cerveja2.setCodigo(2L);
        cerveja1.setValor(valor1);
        cerveja2.setValor(valor2);

        tabelaItensVenda.adicionarItem(cerveja1, 1);
        tabelaItensVenda.adicionarItem(cerveja2, 3);
        assertEquals(new BigDecimal("14.60"), tabelaItensVenda.getValorTotal());

    }

    @Test
    public void deveManterTamanhoDaListaParaMesmasCervejas() throws Exception {
        Cerveja c1 = new Cerveja();
        c1.setCodigo(1L);
        c1.setValor(new BigDecimal("4.50"));
        tabelaItensVenda.adicionarItem(c1, 1);
        tabelaItensVenda.adicionarItem(c1, 1);

        assertEquals(1,tabelaItensVenda.total());
        assertEquals(new BigDecimal("9.00"), tabelaItensVenda.getValorTotal());


    }
}
