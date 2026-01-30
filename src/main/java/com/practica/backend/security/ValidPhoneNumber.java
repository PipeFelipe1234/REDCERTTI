package com.practica.backend.security;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
@Documented
public @interface ValidPhoneNumber {
    String message() default "Número telefónico inválido. Colombia: 10 dígitos (ej: 315 330 3801), Ecuador: 9 dígitos (ej: 99 372 0415)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
