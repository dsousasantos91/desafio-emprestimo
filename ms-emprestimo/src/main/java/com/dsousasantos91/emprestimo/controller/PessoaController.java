package com.dsousasantos91.emprestimo.controller;

import com.dsousasantos91.emprestimo.domain.dto.PessoaRequest;
import com.dsousasantos91.emprestimo.domain.dto.PessoaResponse;
import com.dsousasantos91.emprestimo.event.RecursoCriadoEvent;
import com.dsousasantos91.emprestimo.service.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "Pessoa", description = "API REST - Entidade Pessoa")
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = "/api/v1/pessoa", produces = { "application/json;charset=UTF-8" })
public class PessoaController {

    private final PessoaService pessoaService;
    private final ApplicationEventPublisher publish;

    @Operation(summary = "Cadastro de pessoa.", description = "Registra uma pessoa para que possa posteriormente solicitar empréstimos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitação realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro na requisição."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
            @ApiResponse(responseCode = "500", description = "Falha no servidor.")
    })
    @PostMapping
    public ResponseEntity<PessoaResponse> criar(@Valid @RequestBody PessoaRequest request, HttpServletResponse servletResponse) {
        PessoaResponse response = this.pessoaService.criar(request);
        publish.publishEvent(new RecursoCriadoEvent(this, servletResponse, response.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listagem de pessoas.", description = "Buscar todas as pessoas cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitação realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro na requisição."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
            @ApiResponse(responseCode = "500", description = "Falha no servidor.")
    })
    @GetMapping
    public ResponseEntity<Page<PessoaResponse>> buscarTodos(
            @SortDefault.SortDefaults({ @SortDefault(sort = "id") }) Pageable pageable) {
        Page<PessoaResponse> response = pessoaService.buscarTodos(pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Informações sobre uma pessoa.", description = "Buscar pessoa pelo ID da base de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitação realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro na requisição."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
            @ApiResponse(responseCode = "500", description = "Falha no servidor.")
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<PessoaResponse> buscaoPorId(@PathVariable Long id) {
        PessoaResponse response = pessoaService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualização de pessoa.", description = "Atualizar dados de uma pessoa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitação realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro na requisição."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
            @ApiResponse(responseCode = "500", description = "Falha no servidor.")
    })
    @PutMapping(path = "/{id}")
    public ResponseEntity<PessoaResponse> atualizar(@PathVariable Long id, @Valid @RequestBody PessoaRequest request) {
        PessoaResponse response = this.pessoaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Apaga pessoa.", description = "Excluir definitivamente uma pessoa da base de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitação realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro na requisição."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
            @ApiResponse(responseCode = "500", description = "Falha no servidor.")
    })
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void apagar(@PathVariable Long id) {
        this.pessoaService.apagar(id);
    }
}
