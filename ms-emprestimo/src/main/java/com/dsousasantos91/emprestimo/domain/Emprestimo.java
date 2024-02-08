package com.dsousasantos91.emprestimo.domain;

import com.dsousasantos91.emprestimo.domain.enumeration.StatusPagamento;
import com.dsousasantos91.emprestimo.domain.enumeration.TipoIdentificador;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "emprestimo")
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull(message = "{0} é obrigatório")
    @ManyToOne
    @JoinColumn(name = "id_pessoa")
    private Pessoa pessoa;

    @Column(name = "valor_emprestimo", nullable = false)
    private BigDecimal valorEmprestimo;

    @Column(name = "numero_parcelas", nullable = false)
    private BigDecimal numeroParcelas;

    @Column(name = "status_pagamento", nullable = false)
    private StatusPagamento statusPagamento;

    @Column(name = "data_criacao", nullable = false)
    private LocalDate dataCriacao;
}
