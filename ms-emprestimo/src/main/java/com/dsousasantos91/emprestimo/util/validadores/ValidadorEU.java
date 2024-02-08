package com.dsousasantos91.emprestimo.util.validadores;

import com.dsousasantos91.emprestimo.exception.GenericBadRequestException;

public class ValidadorEU {

    public static boolean isValid(String identificador) {
        identificador = identificador.replaceAll("[^0-9]", "");

        if (identificador.length() != 8) {
            return false;
        }

        int primeiroDigito = Integer.parseInt(identificador.substring(0, 1));
        int ultimoDigito = Integer.parseInt(identificador.substring(7));

        if (primeiroDigito + ultimoDigito != 9) {
            throw new GenericBadRequestException("Identificador de estudante universitário inválido. A soma do primeiro e último digito deve ser igual a 9.");
        }

        return true;
    }
}
