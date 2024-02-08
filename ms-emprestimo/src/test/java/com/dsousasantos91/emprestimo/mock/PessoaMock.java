package com.dsousasantos91.emprestimo.mock;

import com.dsousasantos91.emprestimo.domain.Pessoa;
import com.dsousasantos91.emprestimo.domain.enumeration.TipoIdentificador;

import java.math.BigDecimal;
import java.time.LocalDate;


public class PessoaMock {
    private Pessoa element;
    private PessoaMock(){}

    public static PessoaMock mocked() {
        PessoaMock mock = new PessoaMock();
        initializeDefaultData(mock);
        return mock;
    }

    public static void initializeDefaultData(PessoaMock mock) {
        mock.element = new Pessoa();
        Pessoa element = mock.element;

        element.setId(1L);
        element.setNome("Levi Anthony da Conceição");
        element.setIdentificador("05743780498");
        element.setDataNascimento(LocalDate.of(1979, 1, 13));
        element.setTipoIdentificador(TipoIdentificador.resolverTipoIdentificador(element.getIdentificador()));
        element.setValorMinMensal(element.getTipoIdentificador().getValorMinMensal());
        element.setValorMaxEmprestimo(element.getTipoIdentificador().getValorMaxEmprestimo());
    }

    public PessoaMock withId(Long param) {
        element.setId(param);
        return this;
    }

    public PessoaMock withNome(String param) {
        element.setNome(param);
        return this;
    }

    public PessoaMock withIdentificador(String param) {
        element.setIdentificador(param);
        return this;
    }

    public PessoaMock withDataNascimento(LocalDate param) {
        element.setDataNascimento(param);
        return this;
    }

    public PessoaMock withTipoIdentificador(TipoIdentificador param) {
        element.setTipoIdentificador(param);
        return this;
    }

    public PessoaMock withValorMinMensal(BigDecimal param) {
        element.setValorMinMensal(param);
        return this;
    }

    public PessoaMock withValorMaxEmprestimo(BigDecimal param) {
        element.setValorMaxEmprestimo(param);
        return this;
    }

    public Pessoa mock() {
        return element;
    }
}
