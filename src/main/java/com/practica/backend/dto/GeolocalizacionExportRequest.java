package com.practica.backend.dto;

import java.time.LocalDate;

/**
 * Request para exportar historial de geolocalizaciones a PDF/Excel
 */
public record GeolocalizacionExportRequest(
        LocalDate fechaInicio,
        LocalDate fechaFin,
        Long empleadoId,
        String busqueda,
        String estado,
        String formato // "excel" o "pdf"
) {
}
