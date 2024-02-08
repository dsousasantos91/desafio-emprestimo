package com.dsousasantos91.pagamento.exception;

public class GenericNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Recurso não encontrado";

    public GenericNotFoundException() {
        super(MESSAGE);
    }

    public GenericNotFoundException(String message) {
        super(MESSAGE + ": " + message);
    }
}
