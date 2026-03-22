package com.practica.backend.dto;

import java.time.LocalDateTime;

/**
 * Response para las operaciones de geolocalización
 */
public record SolicitudUbicacionResponse(
        Long solicitudId,
        String estado, // PENDIENTE | RESPONDIDA | EXPIRADA | ERROR
        Long empleadoId,
        String empleadoNombre,
        Double latitud,
        Double longitud,
        Double precisionMetros,
        String ubicacion, // Dirección legible
        LocalDateTime fechaSolicitud,
        LocalDateTime fechaRespuesta,
        String mensaje, // Mensaje informativo opcional
        String mensajeError // Error específico (GPS desactivado, permiso denegado, etc)
) {
    // Constructor simplificado para respuesta de creación
    public SolicitudUbicacionResponse(Long solicitudId, String estado, String mensaje) {
        this(solicitudId, estado, null, null, null, null, null, null, null, null, mensaje, null);
    }

    // Constructor para respuesta completa
    public SolicitudUbicacionResponse(Long solicitudId, String estado, Long empleadoId, String empleadoNombre,
            Double latitud, Double longitud, Double precisionMetros, String ubicacion,
            LocalDateTime fechaSolicitud, LocalDateTime fechaRespuesta) {
        this(solicitudId, estado, empleadoId, empleadoNombre, latitud, longitud, precisionMetros, ubicacion,
                fechaSolicitud, fechaRespuesta, null, null);
    }

    // Constructor para respuesta con error
    public SolicitudUbicacionResponse(Long solicitudId, String estado, Long empleadoId, String empleadoNombre,
            LocalDateTime fechaSolicitud, LocalDateTime fechaRespuesta, String mensajeError) {
        this(solicitudId, estado, empleadoId, empleadoNombre, null, null, null, null,
                fechaSolicitud, fechaRespuesta, null, mensajeError);
    }
}
