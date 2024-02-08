package com.dsousasantos91.emprestimo.domain.dto;

import com.dsousasantos91.emprestimo.domain.enumeration.StatusPagamento;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class EmprestimoResponse {
    private Long id;
    private PessoaResponse pessoa;
    private BigDecimal valorEmprestimo;
    private Integer numeroParcelas;
    private StatusPagamento statusPagamento;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataCriacao;
}
