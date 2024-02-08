package com.dsousasantos91.emprestimo.util.validadores;

import com.dsousasantos91.emprestimo.exception.GenericBadRequestException;

public class ValidadorAP {

    public static boolean isValid(String identificador) {
        identificador = identificador.replaceAll("[^0-9]", "");

        if (identificador.length() != 10) {
            return false;
        }

        String digitosSemUltimo = identificador.substring(0, 9);
        char ultimoDigito = identificador.charAt(9);
        if (digitosSemUltimo.indexOf(ultimoDigito) != -1) {
            throw new GenericBadRequestException("Identificador de aposentado inválido. O ultimo digito não pode se repetir.");
        }
        return true;
    }
}
