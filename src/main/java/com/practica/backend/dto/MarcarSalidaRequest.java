package com.practica.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MarcarSalidaRequest(
        Double latitud,
        Double longitud,
        Double precisionMetros,
        String reporte,
        String picture,
        @JsonProperty("fechaCreacion") String fechaCreacion) {

}
