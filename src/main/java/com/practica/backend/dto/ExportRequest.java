package com.practica.backend.dto;

import java.time.LocalDate;

/**
 * Request para exportar registros con filtros
 * 
 * @param fechaInicio Fecha inicial del rango a exportar
 * @param fechaFin    Fecha final del rango a exportar
 * @param usuarioId   ID del usuario a filtrar (opcional, solo admin)
 * @param busqueda    Texto para buscar por nombre o identificación (opcional)
 * @param mes         Mes a exportar (1-12) - compatibilidad con endpoints
 *                    antiguos
 * @param anio        Año a exportar - compatibilidad con endpoints antiguos
 * @param formato     Formato de exportación: "PDF", "EXCEL", "WORD" -
 *                    compatibilidad
 */
public record ExportRequest(
                LocalDate fechaInicio,
                LocalDate fechaFin,
                Long usuarioId,
                String busqueda,
                Integer mes,
                Integer anio,
                String formato) {
}
