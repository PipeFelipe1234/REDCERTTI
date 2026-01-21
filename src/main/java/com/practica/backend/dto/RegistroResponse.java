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
                String reporte,
                String identificacion) {
}