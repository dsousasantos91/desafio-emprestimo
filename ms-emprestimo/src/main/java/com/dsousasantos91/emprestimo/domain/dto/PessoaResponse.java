package com.dsousasantos91.emprestimo.domain.dto;

import com.dsousasantos91.emprestimo.domain.enumeration.TipoIdentificador;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PessoaResponse {
    private Long id;
    private String nome;
    private String identificador;
    private LocalDate dataNascimento;
    private TipoIdentificador tipoIdentificador;
    private BigDecimal valorMinMensal;
    private BigDecimal valorMaxEmprestimo;
}
