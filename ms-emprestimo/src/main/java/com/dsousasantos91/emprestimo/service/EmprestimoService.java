package com.dsousasantos91.emprestimo.service;

import com.dsousasantos91.emprestimo.domain.Emprestimo;
import com.dsousasantos91.emprestimo.domain.Pessoa;
import com.dsousasantos91.emprestimo.domain.dto.EmprestimoRequest;
import com.dsousasantos91.emprestimo.domain.dto.EmprestimoResponse;
import com.dsousasantos91.emprestimo.domain.enumeration.StatusPagamento;
import com.dsousasantos91.emprestimo.domain.enumeration.TipoIdentificador;
import com.dsousasantos91.emprestimo.exception.GenericBadRequestException;
import com.dsousasantos91.emprestimo.exception.GenericNotFoundException;
import com.dsousasantos91.emprestimo.feign.PagamentoServiceClient;
import com.dsousasantos91.emprestimo.mapper.EmprestimoMapper;
import com.dsousasantos91.emprestimo.repository.EmprestimoRepository;
import com.dsousasantos91.emprestimo.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmprestimoService {
    private final EmprestimoRepository emprestimoRepository;
    private final PessoaRepository pessoaRepository;
    private final EmprestimoMapper emprestimoMapper;
    private final PagamentoServiceClient pagamentoServiceClient;

    public EmprestimoResponse solicitar(EmprestimoRequest request) {
        log.info("Solicitar emprestimo para pessoa {}", request.getIdentificador());
        Pessoa pessoa = this.pessoaRepository.findByIdentificador(request.getIdentificador())
                .orElseThrow(() -> new GenericNotFoundException("Pessoa com identificador [" + request.getIdentificador() + "] não encontrada"));
        this.validarValorSolicitado(pessoa.getTipoIdentificador(), request.getValorEmprestimo());
        this.validarValorParcela(pessoa.getTipoIdentificador(), request);
        Emprestimo emprestimo = Emprestimo.builder()
                .pessoa(pessoa)
                .valorEmprestimo(request.getValorEmprestimo())
                .numeroParcelas(request.getNumeroParcelas())
                .statusPagamento(StatusPagamento.PROCESSANDO)
                .build();
        Emprestimo emprestimoRegistrado = this.emprestimoRepository.save(emprestimo);
        EmprestimoResponse emprestimoPago = pagamentoServiceClient.pagarEmprestimo(emprestimoRegistrado.getId());
        log.info("Pessoa {} solicitou o emprestimo de ID: [{}] com sucesso", emprestimoRegistrado.getPessoa().getNome(), emprestimoRegistrado.getId());
        return emprestimoPago;
    }

    private void validarValorSolicitado(TipoIdentificador tipoIdentificador, BigDecimal valorEmprestimo) {
        if (valorEmprestimo.compareTo(tipoIdentificador.getValorMaxEmprestimo()) > 0)
            throw new GenericBadRequestException("O valor solicitado [" + valorEmprestimo + "] " +
                    "é maior que o permitido [" + tipoIdentificador.getValorMaxEmprestimo() + "] para o tipo de identificador [" + tipoIdentificador.name() + "].");
    }

    private void validarValorParcela(TipoIdentificador tipoIdentificador, EmprestimoRequest request) {
        BigDecimal valorParcela = request.getValorEmprestimo().divide(BigDecimal.valueOf(request.getNumeroParcelas()));
        if (valorParcela.compareTo(tipoIdentificador.getValorMinMensal()) < 0)
            throw new GenericBadRequestException("O valor da parcela [" + valorParcela + "] " +
                    "é menor que a parcela permitida [" + tipoIdentificador.getValorMinMensal() + "] para o tipo de identificador [" + tipoIdentificador.name() + "].");
    }

    public Page<EmprestimoResponse> buscarTodos(Pageable pageable) {
        log.info("Pesquisar emprestimos");
        return this.emprestimoRepository.findAll(pageable).map(emprestimoMapper::toResponse);
    }

    public EmprestimoResponse buscarPorId(Long id) {
        log.info("Buscar emprestimo ID [{}]", id);
        Emprestimo emprestimo = this.emprestimoRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException(String.format("Emprestimo ID: [%d] não encontrada.", id)));
        log.info("Emprestimo ID [{}] encontrada.", emprestimo.getId());
        return this.emprestimoMapper.toResponse(emprestimo);
    }
}
