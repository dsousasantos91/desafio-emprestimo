package com.dsousasantos91.pagamento.service;

import com.dsousasantos91.pagamento.domain.Emprestimo;
import com.dsousasantos91.pagamento.domain.dto.EmprestimoResponse;
import com.dsousasantos91.pagamento.domain.enumeration.StatusPagamento;
import com.dsousasantos91.pagamento.exception.GenericNotFoundException;
import com.dsousasantos91.pagamento.mapper.EmprestimoMapper;
import com.dsousasantos91.pagamento.repository.EmprestimoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmprestimoService {
    private final EmprestimoRepository emprestimoRepository;
    private final EmprestimoMapper emprestimoMapper;

    public EmprestimoResponse pagar(Long idEmprestimo) {
        log.info("Realizar pagamento emprestimo de ID {}", idEmprestimo);
        Emprestimo emprestimo = this.emprestimoRepository.findById(idEmprestimo)
                .orElseThrow(() -> new GenericNotFoundException("Empréstimo com identificador [" + idEmprestimo + "] não encontrado"));
        emprestimo.setStatusPagamento(StatusPagamento.PAGO);
        Emprestimo emprestimoAtualizado = this.emprestimoRepository.save(emprestimo);
        log.info("Empréstimo ID {} pago com sucesso", emprestimoAtualizado.getId());
        return this.emprestimoMapper.toResponse(emprestimoAtualizado);
    }
}
