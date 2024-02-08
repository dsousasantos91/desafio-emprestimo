package com.dsousasantos91.emprestimo.util.validadores;

public class ValidadorEU {

    public static boolean isValid(String identificador) {
        identificador = identificador.replaceAll("[^0-9]", "");

        if (identificador.length() != 8) {
            return false;
        }

        int primeiroDigito = Integer.parseInt(identificador.substring(0, 1));
        int ultimoDigito = Integer.parseInt(identificador.substring(7));

        return primeiroDigito + ultimoDigito == 9;
    }
}
