package com.dsousasantos91.emprestimo.repository;

import com.dsousasantos91.emprestimo.domain.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    Optional<List<Emprestimo>> findByPessoaId(Long idPessoa);
}
