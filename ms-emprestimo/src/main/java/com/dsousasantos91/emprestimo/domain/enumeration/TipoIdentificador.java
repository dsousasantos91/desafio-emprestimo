package com.dsousasantos91.emprestimo.domain.enumeration;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum TipoIdentificador {

    PF(BigDecimal.valueOf(300.0), BigDecimal.valueOf(10000.0)),
    PJ(BigDecimal.valueOf(1000.0), BigDecimal.valueOf(100000.0)),
    AU(BigDecimal.valueOf(100.0), BigDecimal.valueOf(10000.0)),
    AP(BigDecimal.valueOf(400.0), BigDecimal.valueOf(25000.0));

    private final BigDecimal valorMinMensal;
    private final BigDecimal valorMaxEmprestimo;

    TipoIdentificador(BigDecimal valorMinMensal, BigDecimal valorMaxEmprestimo) {
        this.valorMinMensal = valorMinMensal;
        this.valorMaxEmprestimo = valorMaxEmprestimo;
    }
}
