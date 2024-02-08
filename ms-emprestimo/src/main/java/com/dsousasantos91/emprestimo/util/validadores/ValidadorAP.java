package com.dsousasantos91.emprestimo.util.validadores;

public class ValidadorAP {

    public static boolean isValid(String identificador) {
        identificador = identificador.replaceAll("[^0-9]", "");

        if (identificador.length() != 10) {
            return false;
        }

        String digitosSemUltimo = identificador.substring(0, 9);
        char ultimoDigito = identificador.charAt(9);
        return digitosSemUltimo.indexOf(ultimoDigito) == -1;
    }
}
