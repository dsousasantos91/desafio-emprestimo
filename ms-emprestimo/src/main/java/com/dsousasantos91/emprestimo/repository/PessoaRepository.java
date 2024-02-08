package com.dsousasantos91.emprestimo.repository;

import com.dsousasantos91.emprestimo.domain.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    Optional<Pessoa> findByIdentificador(String identificador);
}
