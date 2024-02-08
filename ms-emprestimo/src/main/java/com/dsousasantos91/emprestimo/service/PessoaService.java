package com.dsousasantos91.emprestimo.service;

import com.dsousasantos91.emprestimo.domain.Pessoa;
import com.dsousasantos91.emprestimo.domain.dto.PessoaRequest;
import com.dsousasantos91.emprestimo.domain.dto.PessoaResponse;
import com.dsousasantos91.emprestimo.exception.GenericNotFoundException;
import com.dsousasantos91.emprestimo.mapper.PessoaMapper;
import com.dsousasantos91.emprestimo.repository.PessoaRepository;
import com.dsousasantos91.emprestimo.util.PropertyUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PessoaService {
    private final PessoaRepository pessoaRepository;
    private final PessoaMapper pessoaMapper;

    public PessoaResponse criar(PessoaRequest request) {
        log.info("Criando pessoa {}", request.getNome());
        Pessoa pessoa = this.pessoaMapper.toEntity(request);
        Pessoa pessoaRegistrada = this.pessoaRepository.save(pessoa);
        log.info("Pessoa {} registrada com sucesso. ID: [{}]", pessoaRegistrada.getNome(), pessoaRegistrada.getId());
        return this.pessoaMapper.toResponse(pessoaRegistrada);
    }

    public Page<PessoaResponse> buscarTodos(Pageable pageable) {
        log.info("Pesquisar pessoas");
        return this.pessoaRepository.findAll(pageable).map(pessoaMapper::toResponse);
    }

    public PessoaResponse buscarPorId(Long id) {
        log.info("Buscar pessoa ID [{}]", id);
        Pessoa pessoa = this.pessoaRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException(String.format("Pessoa ID: [%d] não encontrada.", id)));
        log.info("Pessoa ID [{}] encontrada.", pessoa.getId());
        return this.pessoaMapper.toResponse(pessoa);
    }

    public PessoaResponse atualizar(Long id, PessoaRequest request) {
        log.info("Atualizando pessoa ID [{}]", id);
        Pessoa pessoaEncontrada = this.pessoaRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException(String.format("Pessoa ID: [%d] não encontrada.", id)));
        Pessoa pessoa = this.pessoaMapper.toEntity(request);
        PropertyUtils.copyNonNullProperties(pessoa, pessoaEncontrada, "id", "identificador");
        Pessoa pessoaAtualizada = this.pessoaRepository.save(pessoaEncontrada);
        log.info("Pessoa ID [{}] atualizada com sucesso.", pessoaEncontrada.getId());
        return this.pessoaMapper.toResponse(pessoaAtualizada);
    }

    public void apagar(Long id) {
        log.info("Apagando pessoa ID [{}]", id);
        this.pessoaRepository.deleteById(id);
        log.info("Pessoa ID [{}] apagada com sucesso.",id);
    }
}
