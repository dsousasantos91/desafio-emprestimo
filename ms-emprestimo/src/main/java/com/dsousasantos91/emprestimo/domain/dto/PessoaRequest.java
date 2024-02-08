package com.dsousasantos91.emprestimo.domain.dto;

import com.dsousasantos91.emprestimo.util.annotations.Identificador;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PessoaRequest {
    @Size(min = 3, max = 50, message = "É obrigatório digitar um mínimo de {1} para {0}")
    @NotBlank(message = "{0} é obrigatório")
    private String nome;

    @Size(min = 8, max = 14)
    @Identificador
    private String identificador;

    private LocalDate dataNascimento;
}
