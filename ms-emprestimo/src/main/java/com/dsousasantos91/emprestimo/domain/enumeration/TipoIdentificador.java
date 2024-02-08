package com.dsousasantos91.emprestimo.domain.enumeration;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Arrays;

@Getter
public enum TipoIdentificador {

    PF(BigDecimal.valueOf(300.0), BigDecimal.valueOf(10000.0), 11),
    PJ(BigDecimal.valueOf(1000.0), BigDecimal.valueOf(100000.0), 14),
    EU(BigDecimal.valueOf(100.0), BigDecimal.valueOf(10000.0), 8),
    AP(BigDecimal.valueOf(400.0), BigDecimal.valueOf(25000.0), 10);

    private final BigDecimal valorMinMensal;
    private final BigDecimal valorMaxEmprestimo;
    private final Integer tamanho;

    TipoIdentificador(BigDecimal valorMinMensal, BigDecimal valorMaxEmprestimo, Integer tamanho) {
        this.valorMinMensal = valorMinMensal;
        this.valorMaxEmprestimo = valorMaxEmprestimo;
        this.tamanho = tamanho;
    }

    public static TipoIdentificador resolverTipoIdentificador(String identificador) {
        int tamanho = identificador.length();
        return Arrays.stream(TipoIdentificador.values())
                .filter(tipo -> tipo.getTamanho().equals(tamanho))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
