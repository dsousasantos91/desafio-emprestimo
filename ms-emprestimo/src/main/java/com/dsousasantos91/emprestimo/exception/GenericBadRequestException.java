package com.dsousasantos91.emprestimo.exception;

public class GenericBadRequestException extends RuntimeException {

    private static final String MESSAGE = "Requisição inválida";

    public GenericBadRequestException() {
        super(MESSAGE);
    }

    public GenericBadRequestException(String message) {
        super(message);
    }
}
