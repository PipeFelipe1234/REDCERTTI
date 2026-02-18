package com.practica.backend.dto;

import com.practica.backend.security.ValidPhoneNumber;
import jakarta.validation.constraints.Email;

/**
 * Request para actualizar un usuario parcialmente.
 * Todos los campos son opcionales - solo se actualizan los que se envían.
 */
public record UsuarioUpdateRequest(
        String identificacion,
        String nombre,
        @Email String email,
        String rol,
        String foto,
        @ValidPhoneNumber String telefono,
        String cargo) {
}
