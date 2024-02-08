package com.dsousasantos91.emprestimo.service;

import com.dsousasantos91.emprestimo.domain.Emprestimo;
import com.dsousasantos91.emprestimo.domain.Pessoa;
import com.dsousasantos91.emprestimo.domain.dto.EmprestimoRequest;
import com.dsousasantos91.emprestimo.domain.dto.EmprestimoResponse;
import com.dsousasantos91.emprestimo.domain.enumeration.TipoIdentificador;
import com.dsousasantos91.emprestimo.exception.GenericBadRequestException;
import com.dsousasantos91.emprestimo.exception.GenericNotFoundException;
import com.dsousasantos91.emprestimo.feign.PagamentoServiceClient;
import com.dsousasantos91.emprestimo.mapper.EmprestimoMapper;
import com.dsousasantos91.emprestimo.mock.EmprestimoMock;
import com.dsousasantos91.emprestimo.mock.PessoaMock;
import com.dsousasantos91.emprestimo.producer.EmprestimoProducer;
import com.dsousasantos91.emprestimo.repository.EmprestimoRepository;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class EmprestimoServiceTest {
    @MockBean
    private EmprestimoRepository emprestimoRepository;
    @MockBean
    private PessoaRepository pessoaRepository;
    @MockBean
    private PagamentoServiceClient pagamentoServiceClient;
    @MockBean
    private EmprestimoProducer emprestimoProducer;

    @Autowired
    private EmprestimoMapper emprestimoMapper;
    @Autowired
    private EmprestimoService emprestimoService;

    private Emprestimo emprestimo;
    private EmprestimoResponse response;
    private EmprestimoRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emprestimo = EmprestimoMock.mocked().mock();
        response = emprestimoMapper.toResponse(emprestimo);
        request = EmprestimoRequest.builder()
                .identificador(emprestimo.getPessoa().getIdentificador())
                .valorEmprestimo(emprestimo.getValorEmprestimo())
                .numeroParcelas(emprestimo.getNumeroParcelas())
                .build();
    }

    @Test
    void deveSolicitarComSucesso() {
        when(pessoaRepository.findByIdentificador(anyString())).thenReturn(Optional.of(emprestimo.getPessoa()));
        when(emprestimoRepository.save(any())).thenReturn(emprestimo);
        when(pagamentoServiceClient.pagarEmprestimo(anyLong())).thenReturn(response);

        EmprestimoResponse result = emprestimoService.solicitar(request, Boolean.FALSE);
        assertEquals(response.getId(), result.getId());
        assertEquals(response.getIdPessoa(), result.getIdPessoa());
        assertEquals(response.getValorEmprestimo(), result.getValorEmprestimo());
        assertEquals(response.getNumeroParcelas(), result.getNumeroParcelas());
        assertEquals(response.getDataCriacao(), result.getDataCriacao());
        assertEquals(response.getStatusPagamento(), result.getStatusPagamento());
        verify(pagamentoServiceClient, times(1)).pagarEmprestimo(anyLong());
    }

    @Test
    void deveSolicitarAsyncComSucesso() {
        when(pessoaRepository.findByIdentificador(anyString())).thenReturn(Optional.of(emprestimo.getPessoa()));
        when(emprestimoRepository.save(any())).thenReturn(emprestimo);

        EmprestimoResponse result = emprestimoService.solicitar(request, Boolean.TRUE);
        assertEquals(response.getId(), result.getId());
        assertEquals(response.getIdPessoa(), result.getIdPessoa());
        assertEquals(response.getValorEmprestimo(), result.getValorEmprestimo());
        assertEquals(response.getNumeroParcelas(), result.getNumeroParcelas());
        assertEquals(response.getDataCriacao(), result.getDataCriacao());
        assertEquals(response.getStatusPagamento(), result.getStatusPagamento());
        verify(emprestimoProducer, times(1)).enviarEmprestimoParaPagamento(anyLong());
    }

    @Test
    void deveValidarLimiteDisponivelAoSolicitarEmprestimo() {
        request.setValorEmprestimo(BigDecimal.valueOf(3001));
        Emprestimo emprestimo2 = EmprestimoMock.mocked().withId(2L).withValorEmprestimo(BigDecimal.valueOf(2000)).mock();
        List<Emprestimo> emprestimos = Arrays.asList(emprestimo, emprestimo2);
        BigDecimal valorTotalDeEmprestimos = emprestimos.stream().map(Emprestimo::getValorEmprestimo).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal valorDisponivel = emprestimo.getPessoa().getTipoIdentificador().getValorMaxEmprestimo().subtract(valorTotalDeEmprestimos);

        when(pessoaRepository.findByIdentificador(anyString())).thenReturn(Optional.of(emprestimo.getPessoa()));
        when(emprestimoRepository.save(any())).thenReturn(emprestimo);
        when(emprestimoRepository.findByPessoaId(anyLong())).thenReturn(Optional.of(emprestimos));
        when(pagamentoServiceClient.pagarEmprestimo(anyLong())).thenReturn(response);

        GenericBadRequestException exception = assertThrows(GenericBadRequestException.class, () -> emprestimoService.solicitar(request, Boolean.FALSE));
        assertEquals(exception.getMessage(), "O valor solicitado [" + request.getValorEmprestimo() + "] " +
                "é maior que o valor disponível [" + valorDisponivel + "] para a pessoa com identificador [" + emprestimo.getPessoa().getIdentificador() + "].");
    }

    @Test
    void deveValidarValorEmprestimoSolicitadoMaiorQueOPermitidoParaOTipoIdentificador() {
        when(pessoaRepository.findByIdentificador(anyString())).thenReturn(Optional.of(emprestimo.getPessoa()));
        when(emprestimoRepository.save(any())).thenReturn(emprestimo);
        when(pagamentoServiceClient.pagarEmprestimo(anyLong())).thenReturn(response);

        request.setValorEmprestimo(BigDecimal.valueOf(10000.1));
        TipoIdentificador tipoIdentificador = emprestimo.getPessoa().getTipoIdentificador();

        GenericBadRequestException exception = assertThrows(GenericBadRequestException.class, () -> emprestimoService.solicitar(request, Boolean.FALSE));
        assertEquals(exception.getMessage(), "O valor solicitado [" + request.getValorEmprestimo() + "] " +
                "é maior que o permitido [" + tipoIdentificador.getValorMaxEmprestimo() + "] para o tipo de identificador [" + tipoIdentificador.name() + "].");
    }

    @Test
    void deveValidarValorParcelaMenorQueOPermitidoParaOTipoIdentificador() {
        when(pessoaRepository.findByIdentificador(anyString())).thenReturn(Optional.of(emprestimo.getPessoa()));
        when(emprestimoRepository.save(any())).thenReturn(emprestimo);
        when(pagamentoServiceClient.pagarEmprestimo(anyLong())).thenReturn(response);

        request.setNumeroParcelas(24);
        TipoIdentificador tipoIdentificador = emprestimo.getPessoa().getTipoIdentificador();
        BigDecimal valorParcela = request.getValorEmprestimo().divide(BigDecimal.valueOf(request.getNumeroParcelas()), RoundingMode.HALF_EVEN);

        GenericBadRequestException exception = assertThrows(GenericBadRequestException.class, () -> emprestimoService.solicitar(request, Boolean.FALSE));
        assertEquals(exception.getMessage(), "O valor da parcela [" + valorParcela + "] " +
                "é menor que a parcela permitida [" + tipoIdentificador.getValorMinMensal() + "] para o tipo de identificador [" + tipoIdentificador.name() + "].");
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
        Emprestimo emprestimo2 = EmprestimoMock.mocked()
                .withId(2L)
                .withPessoa(pessoa2)
                .mock();
        Emprestimo emprestimo3 = EmprestimoMock.mocked()
                .withId(3L)
                .withPessoa(pessoa3)
                .mock();
        PageRequest pageable = PageRequest.of(0, 2);
        List<Emprestimo> emprestimos = Arrays.asList(emprestimo, emprestimo2, emprestimo3);
        PageImpl<Emprestimo> pageResponse = new PageImpl<>(emprestimos, pageable, pageable.getPageSize());
        when(emprestimoRepository.findAll(any(Pageable.class))).thenReturn(pageResponse);
        Page<EmprestimoResponse> response = emprestimoService.buscarTodos(pageable);
        List<Long> idsEmprestimos = response.stream().map(EmprestimoResponse::getId).collect(toList());
        assertEquals(response.getPageable().getPageNumber(), pageable.getPageNumber());
        assertEquals(response.getPageable().getPageSize(), pageable.getPageSize());
        assertEquals(response.getTotalElements(), 2);
        assertTrue(idsEmprestimos.contains(emprestimo.getId()));
        assertTrue(idsEmprestimos.contains(emprestimo2.getId()));
    }

    @Test
    void deveLancarGenericNotFoundExceptionAoSolicitarEmprestimo() {
        when(pessoaRepository.findByIdentificador(anyString())).thenReturn(Optional.empty());
        GenericNotFoundException exception = assertThrows(GenericNotFoundException.class, () -> emprestimoService.solicitar(request, Boolean.FALSE));
        assertEquals(exception.getMessage(), "Pessoa com identificador [" + request.getIdentificador() + "] não encontrada");
    }

    @Test
    void deveBuscarPorIdComSucesso() {
        when(emprestimoRepository.findById(any())).thenReturn(Optional.of(emprestimo));

        EmprestimoResponse result = emprestimoService.buscarPorId(1L);
        assertEquals(response.getId(), result.getId());
        assertEquals(response.getIdPessoa(), result.getIdPessoa());
        assertEquals(response.getValorEmprestimo(), result.getValorEmprestimo());
        assertEquals(response.getNumeroParcelas(), result.getNumeroParcelas());
        assertEquals(response.getDataCriacao(), result.getDataCriacao());
        assertEquals(response.getStatusPagamento(), result.getStatusPagamento());
    }

    @Test
    void deveLancarGenericNotFoundExceptionAoBuscarPorId() {
        when(emprestimoRepository.findById(anyLong())).thenReturn(Optional.empty());
        GenericNotFoundException exception = assertThrows(GenericNotFoundException.class, () -> emprestimoService.buscarPorId(2L));
        assertEquals(exception.getMessage(), "Emprestimo ID: [2] não encontrada.");
    }
}
