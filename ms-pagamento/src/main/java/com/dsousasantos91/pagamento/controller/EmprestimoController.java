package com.dsousasantos91.pagamento.controller;


import com.dsousasantos91.pagamento.domain.dto.EmprestimoResponse;
import com.dsousasantos91.pagamento.service.EmprestimoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Emprestimo", description = "API REST - Entidade Emprestimo")
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = "/api/v1/pagamento", produces = { "application/json;charset=UTF-8" })
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    @Operation(summary = "Pagar emprestimo.", description = "Pagar um emprestimo por id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitação realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro na requisição."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
            @ApiResponse(responseCode = "500", description = "Falha no servidor.")
    })
    @PostMapping(path = "/emprestimo/{id}/pagar")
    public ResponseEntity<EmprestimoResponse> pagar(@PathVariable Long id) {
        EmprestimoResponse response = this.emprestimoService.pagar(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
