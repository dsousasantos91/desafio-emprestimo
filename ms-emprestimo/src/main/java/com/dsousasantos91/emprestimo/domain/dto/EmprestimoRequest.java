package com.dsousasantos91.emprestimo.domain.dto;

import com.dsousasantos91.emprestimo.util.annotations.Identificador;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmprestimoRequest {

    @Identificador
    private String identificador;
    private BigDecimal valorEmprestimo;

    @Max(value = 24, message = "{0} deve ser menor ou igual a {value}")
    private Integer numeroParcelas;
}
