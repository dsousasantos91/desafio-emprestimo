package com.dsousasantos91.emprestimo.exception;

public class GenericNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Recurso não encontrado";

    public GenericNotFoundException() {
        super(MESSAGE);
    }

    public GenericNotFoundException(String message) {
        super(message);
    }
}
