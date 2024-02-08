package com.dsousasantos91.emprestimo.util.validadores;

import com.dsousasantos91.emprestimo.exception.GenericBadRequestException;

public class ValidadorCNPJ {

    public static boolean isValid(String cnpj) {
        cnpj = cnpj.replaceAll("[^0-9]", "");

        if (cnpj.length() != 14) {
            return false;
        }

        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        int soma = 0;
        int peso = 2;
        for (int i = 11; i >= 0; i--) {
            soma += Integer.parseInt(cnpj.substring(i, i + 1)) * peso;
            peso++;
            if (peso == 10) {
                peso = 2;
            }
        }
        int digito1 = 11 - (soma % 11);
        if (digito1 > 9) {
            digito1 = 0;
        }
        if (Integer.parseInt(cnpj.substring(12, 13)) != digito1) {
            throw new GenericBadRequestException("CNPJ inválido");
        }

        soma = 0;
        peso = 2;
        for (int i = 12; i >= 0; i--) {
            soma += Integer.parseInt(cnpj.substring(i, i + 1)) * peso;
            peso++;
            if (peso == 10) {
                peso = 2;
            }
        }
        int digito2 = 11 - (soma % 11);
        if (digito2 > 9) {
            digito2 = 0;
        }
        if (Integer.parseInt(cnpj.substring(13)) != digito2) {
            throw new GenericBadRequestException("CNPJ inválido");
        }

        return true;
    }
}
