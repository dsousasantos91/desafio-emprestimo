package com.dsousasantos91.emprestimo.service;

import com.dsousasantos91.emprestimo.domain.Pessoa;
import com.dsousasantos91.emprestimo.domain.dto.PessoaRequest;
import com.dsousasantos91.emprestimo.domain.dto.PessoaResponse;
import com.dsousasantos91.emprestimo.exception.GenericNotFoundException;
import com.dsousasantos91.emprestimo.mapper.PessoaMapper;
import com.dsousasantos91.emprestimo.mock.PessoaMock;
import com.dsousasantos91.emprestimo.repository.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class PessoaServiceTest {
    @MockBean
    private PessoaRepository pessoaRepository;
    @Autowired
    private PessoaMapper pessoaMapper;
    @Autowired
    private PessoaService pessoaService;

    private Pessoa pessoa;
    private PessoaResponse response;
    private PessoaRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pessoa = PessoaMock.mocked().mock();
        response = pessoaMapper.toResponse(pessoa);
        request = PessoaRequest.builder()
                .nome(pessoa.getNome())
                .identificador(pessoa.getIdentificador())
                .dataNascimento(pessoa.getDataNascimento())
                .build();
    }

    @Test
    void deveCriarPessoaComSucesso() {
        when(pessoaRepository.save(any())).thenReturn(pessoa);

        PessoaResponse result = pessoaService.criar(request);
        assertEquals(response.getId(), result.getId());
        assertEquals(response.getIdentificador(), result.getIdentificador());
        assertEquals(response.getNome(), result.getNome());
        assertEquals(response.getTipoIdentificador(), result.getTipoIdentificador());
        assertEquals(response.getDataNascimento(), result.getDataNascimento());
        assertEquals(response.getValorMinMensal(), result.getValorMinMensal());
        assertEquals(response.getValorMaxEmprestimo(), result.getValorMaxEmprestimo());
    }

    @Test
    void deveBuscarTodosComSucesso() {
        Pessoa pessoa2 = PessoaMock.mocked().withId(2L)
                .withNome("Theo Murilo da Rocha")
                .withIdentificador("12345678")
                .mock();
        Pessoa pessoa3 = PessoaMock.mocked().withId(3L)
                .withNome("Alícia Adriana Ferreira")
                .withIdentificador("97661674351")
                .mock();
        PageRequest pageable = PageRequest.of(0, 2);
        List<Pessoa> pessoas = Arrays.asList(pessoa, pessoa2, pessoa3);
        PageImpl<Pessoa> pageResponse = new PageImpl<>(pessoas, pageable, pageable.getPageSize());
        when(pessoaRepository.findAll(any(Pageable.class))).thenReturn(pageResponse);
        Page<PessoaResponse> response = pessoaService.buscarTodos(pageable);
        List<Long> idsPessoas = response.stream().map(PessoaResponse::getId).collect(toList());
        assertEquals(response.getPageable().getPageNumber(), pageable.getPageNumber());
        assertEquals(response.getPageable().getPageSize(), pageable.getPageSize());
        assertEquals(response.getTotalElements(), 2);
        assertTrue(idsPessoas.contains(pessoa.getId()));
        assertTrue(idsPessoas.contains(pessoa2.getId()));
    }

    @Test
    void deveBuscarPorIdComSucesso() {
        when(pessoaRepository.findById(any())).thenReturn(Optional.of(pessoa));

        PessoaResponse result = pessoaService.buscarPorId(1L);
        assertEquals(response.getId(), result.getId());
        assertEquals(response.getIdentificador(), result.getIdentificador());
        assertEquals(response.getNome(), result.getNome());
        assertEquals(response.getTipoIdentificador(), result.getTipoIdentificador());
        assertEquals(response.getDataNascimento(), result.getDataNascimento());
        assertEquals(response.getValorMinMensal(), result.getValorMinMensal());
        assertEquals(response.getValorMaxEmprestimo(), result.getValorMaxEmprestimo());
    }

    @Test
    void deveAtualizarAtualizarComSucesso() {
        String nomeAnterior = pessoa.getNome();
        Pessoa pessoaResult = PessoaMock.mocked().withNome("Theo Murilo da Rocha").mock();
        when(pessoaRepository.findById(any())).thenReturn(Optional.of(pessoa));
        when(pessoaRepository.save(any())).thenReturn(pessoaResult);

        request.setNome("Theo Murilo da Rocha");
        PessoaResponse result = pessoaService.atualizar(1L, request);
        assertEquals(request.getNome(), result.getNome());
        assertNotEquals(nomeAnterior, result.getNome());
    }

    @Test
    void deveLancarGenericNotFoundExceptionAoAtualizar() {
        when(pessoaRepository.findById(anyLong())).thenReturn(Optional.empty());
        GenericNotFoundException exception = assertThrows(GenericNotFoundException.class, () -> pessoaService.atualizar(2L, request));
        assertEquals(exception.getMessage(), "Pessoa ID: [2] não encontrada.");
    }

    @Test
    void deveLancarGenericNotFoundExceptionAoBuscarPorId() {
        when(pessoaRepository.findById(anyLong())).thenReturn(Optional.empty());
        GenericNotFoundException exception = assertThrows(GenericNotFoundException.class, () -> pessoaService.buscarPorId(2L));
        assertEquals(exception.getMessage(), "Pessoa ID: [2] não encontrada.");
    }

    @Test
    void deveApagarComSucesso() {
        pessoaService.apagar(1L);
        verify(pessoaRepository, times(1)).deleteById(1L);
    }
}

