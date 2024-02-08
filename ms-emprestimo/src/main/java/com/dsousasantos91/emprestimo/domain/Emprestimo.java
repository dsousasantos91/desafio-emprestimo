package com.dsousasantos91.emprestimo.domain;

import com.dsousasantos91.emprestimo.domain.enumeration.StatusPagamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
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
    private Integer numeroParcelas;

    @Column(name = "status_pagamento", nullable = false)
    private StatusPagamento statusPagamento;

    @Column(name = "data_criacao", nullable = false)
    private LocalDate dataCriacao;

    @PrePersist
    public void inicializarParametrosIniciais() {
        this.dataCriacao = LocalDate.now();
    }
}
