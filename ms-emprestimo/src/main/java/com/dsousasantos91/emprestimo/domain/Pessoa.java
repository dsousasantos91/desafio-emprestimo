package com.dsousasantos91.emprestimo.domain;

import com.dsousasantos91.emprestimo.domain.enumeration.TipoIdentificador;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
@Entity
@Table(name = "PESSOA", uniqueConstraints = @UniqueConstraint(name = "UniqueAssociado", columnNames = { "identificador" }))
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(min = 3, max = 50, message = "É obrigatório digitar um mínimo de {1} para {0}")
    @NotBlank(message = "{0} é obrigatório")
    @Column(name = "nome", nullable = false)
    private String nome;

    @Size(min = 11, max = 11)
    @Pattern(regexp = "(^\\d{14}$)", message = "Enviar apenas números para {0}")
    @Column(name = "identificador", nullable = false)
    private String identificador;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "tipo_identificador", nullable = false)
    private TipoIdentificador tipoIdentificador;

    @Column(name = "valor_min_mensal", nullable = false)
    private BigDecimal valorMinMensalParcela;

    @Column(name = "valor_max_emprestimo", nullable = false)
    private BigDecimal valorMaxEmprestimo;
}
