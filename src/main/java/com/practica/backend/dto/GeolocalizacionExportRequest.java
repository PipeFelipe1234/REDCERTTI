package com.practica.backend.dto;

import java.time.LocalDate;

/**
 * Request para exportar historial de geolocalizaciones a PDF/Excel
 * 
 * Uso simple por mes: {"mes": 3} (marzo del año actual)
 * Uso con año: {"mes": 3, "anio": 2026}
 * Uso con filtros avanzados: {"fechaInicio": "2026-03-01", "fechaFin":
 * "2026-03-31", ...}
 */
public record GeolocalizacionExportRequest(
                Integer mes, // 1-12, si se especifica exporta solo ese mes
                Integer anio, // Año, si no se especifica usa el actual
                LocalDate fechaInicio,
                LocalDate fechaFin,
                Long empleadoId,
                String busqueda,
                String estado,
                String formato // "excel" o "pdf"
) {
}
