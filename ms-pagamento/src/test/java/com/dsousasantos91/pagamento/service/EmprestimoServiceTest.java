package com.dsousasantos91.pagamento.service;

import com.dsousasantos91.pagamento.domain.Emprestimo;
import com.dsousasantos91.pagamento.domain.dto.EmprestimoResponse;
import com.dsousasantos91.pagamento.domain.enumeration.StatusPagamento;
import com.dsousasantos91.pagamento.exception.GenericNotFoundException;
import com.dsousasantos91.pagamento.mapper.EmprestimoMapper;
import com.dsousasantos91.pagamento.mock.EmprestimoMock;
import com.dsousasantos91.pagamento.repository.EmprestimoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class EmprestimoServiceTest {
    @MockBean
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private EmprestimoMapper emprestimoMapper;
    @Autowired
    private EmprestimoService emprestimoService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void devePagarComSucesso() {
        Emprestimo emprestimo = EmprestimoMock.mocked().mock();
        EmprestimoResponse response = this.emprestimoMapper.toResponse(emprestimo);
        response.setStatusPagamento(StatusPagamento.PAGO);

        Mockito.when(emprestimoRepository.findById(anyLong())).thenReturn(Optional.of(emprestimo));
        Mockito.when(emprestimoRepository.save(any())).thenReturn(emprestimo);

        EmprestimoResponse result = emprestimoService.pagar(1L);
        assertEquals(response.getId(), result.getId());
        assertEquals(response.getIdPessoa(), result.getIdPessoa());
        assertEquals(response.getValorEmprestimo(), result.getValorEmprestimo());
        assertEquals(response.getNumeroParcelas(), result.getNumeroParcelas());
        assertEquals(response.getDataCriacao(), result.getDataCriacao());
        assertEquals(response.getStatusPagamento(), result.getStatusPagamento());
    }

    @Test
    void deveLancarGenericNotFoundExceptionAoPagarEmprestimo() {
        when(emprestimoRepository.findById(anyLong())).thenReturn(Optional.empty());
        GenericNotFoundException exception = assertThrows(GenericNotFoundException.class, () -> emprestimoService.pagar(2L));
        assertEquals(exception.getMessage(), "Empréstimo com identificador [" + 2 + "] não encontrado");
    }
}
