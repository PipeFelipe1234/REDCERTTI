package com.practica.backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record RegistroResponse(
                Long id,
                LocalDate fecha,
                LocalTime horaEntrada,
                LocalTime horaSalida,
                Double latitud,
                Double longitud,
                Double precisionMetros,
                Double latitudCheckin,
                Double longitudCheckin,
                Double precisionMetrosCheckin,
                String reporte,
                String picture,
                String identificacion,
                String nombre,
                String foto,
                String telefono,
                String horasTrabajadas, // Formato "HH:mm:ss" - calculado en tiempo real o final
                Long minutosTrabajados, // Total en minutos - para cálculos
                Boolean enCurso // true si aún no ha marcado salida
) {
}