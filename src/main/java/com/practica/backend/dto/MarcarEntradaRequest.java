package com.practica.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MarcarEntradaRequest(
                Double latitudCheckin,
                Double longitudCheckin,
                Double precisionMetrosCheckin,
                @JsonProperty("fechaCreacion") String fechaCreacion) {
}
