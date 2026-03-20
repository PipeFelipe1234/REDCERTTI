package com.practica.backend.dto;

import java.time.LocalDateTime;

/**
 * Response para el historial de geolocalizaciones
 * Incluye información completa de cada solicitud para exportación y listado
 */
public record GeolocalizacionHistorialResponse(
        Long id,
        String estado,
        // Datos del Admin que solicitó
        Long adminId,
        String adminNombre,
        String adminIdentificacion,
        // Datos del Empleado localizado
        Long empleadoId,
        String empleadoNombre,
        String empleadoIdentificacion,
        // Datos de ubicación
        Double latitud,
        Double longitud,
        Double precisionMetros,
        String ubicacion,
        // Fechas
        LocalDateTime fechaSolicitud,
        LocalDateTime fechaRespuesta,
        // Error si aplica
        String mensajeError) {

    /**
     * Constructor desde entidad SolicitudUbicacion
     */
    public static GeolocalizacionHistorialResponse fromEntity(
            com.practica.backend.entity.SolicitudUbicacion solicitud) {
        return new GeolocalizacionHistorialResponse(
                solicitud.getId(),
                solicitud.getEstado(),
                solicitud.getAdmin().getId(),
                solicitud.getAdmin().getNombre(),
                solicitud.getAdmin().getIdentificacion(),
                solicitud.getEmpleado().getId(),
                solicitud.getEmpleado().getNombre(),
                solicitud.getEmpleado().getIdentificacion(),
                solicitud.getLatitud(),
                solicitud.getLongitud(),
                solicitud.getPrecisionMetros(),
                solicitud.getUbicacion(),
                solicitud.getFechaSolicitud(),
                solicitud.getFechaRespuesta(),
                solicitud.getMensajeError());
    }
}
