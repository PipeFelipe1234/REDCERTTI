package com.practica.backend.dto;

/**
 * Request para que el empleado responda a una solicitud de ubicación
 */
public record ResponderUbicacionRequest(
        Long solicitudId,
        Double latitud,
        Double longitud,
        Double precisionMetros,
        String ubicacion, // Dirección legible (reverse geocoding desde el frontend)
        Boolean error, // true si hubo error al obtener ubicación
        String mensajeError // Ej: "GPS desactivado", "Permiso denegado", etc.
) {
    // Constructor para respuesta exitosa (sin error)
    public ResponderUbicacionRequest(Long solicitudId, Double latitud, Double longitud,
            Double precisionMetros, String ubicacion) {
        this(solicitudId, latitud, longitud, precisionMetros, ubicacion, false, null);
    }

    // Constructor para respuesta con error
    public ResponderUbicacionRequest(Long solicitudId, String mensajeError) {
        this(solicitudId, null, null, null, null, true, mensajeError);
    }
}
