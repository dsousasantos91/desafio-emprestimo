package com.dsousasantos91.emprestimo.mapper;

import com.dsousasantos91.emprestimo.domain.Emprestimo;
import com.dsousasantos91.emprestimo.domain.dto.EmprestimoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface EmprestimoMapper {

    @Mapping(source = "pessoa.id", target = "idPessoa")
    EmprestimoResponse toResponse(Emprestimo emprestimo);
}
