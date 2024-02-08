package com.dsousasantos91.emprestimo.util.annotations;

import com.dsousasantos91.emprestimo.util.validadores.ValidadorAP;
import com.dsousasantos91.emprestimo.util.validadores.ValidadorCNPJ;
import com.dsousasantos91.emprestimo.util.validadores.ValidadorCPF;
import com.dsousasantos91.emprestimo.util.validadores.ValidadorEU;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class IdentificadorValidator implements ConstraintValidator<Identificador, String> {

    @Override
    public void initialize(Identificador constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(value);
        boolean containsNonDigits = matcher.find();
        if (containsNonDigits) {
            return false;
        }
        if (value.length() == 11) {
            return ValidadorCPF.isValid(value);
        }
        if (value.length() == 14) {
            return ValidadorCNPJ.isValid(value);
        }
        if (value.length() == 8) {
            return ValidadorEU.isValid(value);
        }
        if (value.length() == 10) {
            return ValidadorAP.isValid(value);
        }
        return false;
    }
}

