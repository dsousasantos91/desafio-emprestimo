package com.dsousasantos91.emprestimo.mapper;

import com.dsousasantos91.emprestimo.domain.Pessoa;
import com.dsousasantos91.emprestimo.domain.dto.PessoaRequest;
import com.dsousasantos91.emprestimo.domain.dto.PessoaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PessoaMapper {
    PessoaResponse toResponse(Pessoa pessoa);
    Pessoa toEntity(PessoaRequest request);
}
