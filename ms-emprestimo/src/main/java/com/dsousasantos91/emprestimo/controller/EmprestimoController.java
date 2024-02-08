package com.dsousasantos91.emprestimo.controller;

import com.dsousasantos91.emprestimo.domain.dto.EmprestimoRequest;
import com.dsousasantos91.emprestimo.domain.dto.EmprestimoResponse;
import com.dsousasantos91.emprestimo.event.RecursoCriadoEvent;
import com.dsousasantos91.emprestimo.service.EmprestimoService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "Emprestimo", description = "API REST - Entidade Emprestimo")
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = "/api/v1/emprestimo", produces = { "application/json;charset=UTF-8" })
public class EmprestimoController {

    private final EmprestimoService emprestimoService;
    private final ApplicationEventPublisher publish;

    @Operation(summary = "Solicitar emprestimo.", description = "Solicitar um emprestimo considerando os limites para o tipo de identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitação realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro na requisição."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
            @ApiResponse(responseCode = "500", description = "Falha no servidor.")
    })
    @PostMapping
    public ResponseEntity<EmprestimoResponse> solicitar(@Valid @RequestBody EmprestimoRequest request,
                                                        HttpServletResponse servletResponse) {
        EmprestimoResponse response = this.emprestimoService.solicitar(request, Boolean.FALSE);
        publish.publishEvent(new RecursoCriadoEvent(this, servletResponse, response.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Solicitar emprestimo async.", description = "Solicitar um emprestimo async considerando os limites para o tipo de identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitação realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro na requisição."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
            @ApiResponse(responseCode = "500", description = "Falha no servidor.")
    })
    @PostMapping(path = "/async")
    public ResponseEntity<EmprestimoResponse> solicitarAsync(@Valid @RequestBody EmprestimoRequest request,
                                                        HttpServletResponse servletResponse) {
        EmprestimoResponse response = this.emprestimoService.solicitar(request, Boolean.TRUE);
        publish.publishEvent(new RecursoCriadoEvent(this, servletResponse, response.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listagem de empréstimos.", description = "Buscar todas os empréstimos solicitados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitação realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro na requisição."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
            @ApiResponse(responseCode = "500", description = "Falha no servidor.")
    })
    @GetMapping
    public ResponseEntity<Page<EmprestimoResponse>> buscarTodos(
            @SortDefault.SortDefaults({ @SortDefault(sort = "id") }) Pageable pageable) {
        Page<EmprestimoResponse> response = this.emprestimoService.buscarTodos(pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Informações sobre um empréstimo.", description = "Buscar empréstimo pelo ID da base de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitação realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro na requisição."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
            @ApiResponse(responseCode = "500", description = "Falha no servidor.")
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<EmprestimoResponse> buscaoPorId(@PathVariable Long id) {
        EmprestimoResponse response = this.emprestimoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }
}
