package com.dsousasantos91.emprestimo.feign;

import com.dsousasantos91.emprestimo.domain.dto.EmprestimoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "pagamentoService", url = "${pagamento-service.url}")
public interface PagamentoServiceClient {
    @PostMapping(path = "/emprestimo/{id}/pagar", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    EmprestimoResponse pagarEmprestimo(@PathVariable Long id);
}
