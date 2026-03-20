package com.practica.backend.dto;

import java.time.LocalDate;

/**
 * Request para filtrar el historial de geolocalizaciones
 */
public record GeolocalizacionFilterRequest(
        LocalDate fechaInicio,
        LocalDate fechaFin,
        Long empleadoId,
        String busqueda, // Buscar por nombre o identificación del empleado
        String estado // PENDIENTE | RESPONDIDA | EXPIRADA | ERROR | null para todos
) {
}
