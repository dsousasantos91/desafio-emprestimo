package com.dsousasantos91.emprestimo.mock;

import com.dsousasantos91.emprestimo.domain.Emprestimo;
import com.dsousasantos91.emprestimo.domain.Pessoa;
import com.dsousasantos91.emprestimo.domain.enumeration.StatusPagamento;

import java.math.BigDecimal;
import java.time.LocalDate;


public class EmprestimoMock {
    private Emprestimo element;
    private EmprestimoMock(){}

    public static EmprestimoMock mocked() {
        EmprestimoMock mock = new EmprestimoMock();
        initializeDefaultData(mock);
        return mock;
    }

    public static void initializeDefaultData(EmprestimoMock mock) {
        mock.element = new Emprestimo();
        Emprestimo element = mock.element;

        element.setId(1L);
        element.setPessoa(PessoaMock.mocked().mock());
        element.setValorEmprestimo(BigDecimal.valueOf(5000.0));
        element.setNumeroParcelas(10);
        element.setStatusPagamento(StatusPagamento.PAGO);
        element.setDataCriacao(LocalDate.now());
    }

    public EmprestimoMock withId(Long param) {
        element.setId(param);
        return this;
    }

    public EmprestimoMock withPessoa(Pessoa param) {
        element.setPessoa(param);
        return this;
    }

    public EmprestimoMock withValorEmprestimo(BigDecimal param) {
        element.setValorEmprestimo(param);
        return this;
    }

    public EmprestimoMock withNumeroParcelas(Integer param) {
        element.setNumeroParcelas(param);
        return this;
    }

    public EmprestimoMock withStatusPagamento(StatusPagamento param) {
        element.setStatusPagamento(param);
        return this;
    }

    public EmprestimoMock withDataCriacao(LocalDate param) {
        element.setDataCriacao(param);
        return this;
    }

    public Emprestimo mock() {
        return element;
    }
}
