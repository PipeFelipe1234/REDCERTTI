package com.practica.backend.security;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Si es nulo o vacío, permitir (usa @NotNull si es requerido)
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        // Remover espacios, guiones y paréntesis para validación
        String cleanedPhone = value.replaceAll("[\\s\\-()]", "");

        // Validar que solo contenga dígitos
        if (!cleanedPhone.matches("\\d+")) {
            return false;
        }

        // Colombia: 10 dígitos (ej: 315 330 3801)
        // Ecuador: 9 dígitos (ej: 99 372 0415)
        boolean isValidColombia = cleanedPhone.length() == 10;
        boolean isValidEcuador = cleanedPhone.length() == 9;

        return isValidColombia || isValidEcuador;
    }
}
