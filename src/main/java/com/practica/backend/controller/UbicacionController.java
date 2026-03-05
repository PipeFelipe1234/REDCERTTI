package com.practica.backend.controller;

import com.practica.backend.dto.ResponderUbicacionRequest;
import com.practica.backend.dto.SolicitudUbicacionResponse;
import com.practica.backend.entity.Usuario;
import com.practica.backend.service.GeolocalizacionService;
import com.practica.backend.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ubicacion")
@CrossOrigin(origins = "*")
public class UbicacionController {

    private static final Logger logger = LoggerFactory.getLogger(UbicacionController.class);

    private final GeolocalizacionService geolocalizacionService;
    private final UsuarioService usuarioService;

    public UbicacionController(GeolocalizacionService geolocalizacionService, UsuarioService usuarioService) {
        this.geolocalizacionService = geolocalizacionService;
        this.usuarioService = usuarioService;
    }

    /**
     * Empleado responde a una solicitud de ubicación
     * La app del empleado llama a este endpoint cuando recibe la notificación
     * silenciosa
     * 
     * Body esperado:
     * {
     * "solicitudId": 123,
     * "latitud": 1.2136,
     * "longitud": -77.2811,
     * "precisionMetros": 8.5,
     * "ubicacion": "Calle 18, Centro, Pasto, Nariño"
     * }
     */
    @PostMapping("/responder")
    public ResponseEntity<?> responderSolicitud(@RequestBody ResponderUbicacionRequest request) {
        logger.info("📍 Empleado respondiendo solicitud ID: {}", request.solicitudId());

        String identificacion = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario empleado = usuarioService.obtenerPorIdentificacion(identificacion);

        geolocalizacionService.responderSolicitud(empleado, request);

        return ResponseEntity.ok(Map.of(
                "mensaje", "Ubicación enviada correctamente",
                "solicitudId", request.solicitudId()));
    }

    /**
     * Empleado consulta si tiene solicitudes pendientes
     * La app puede llamar a este endpoint periódicamente o al iniciar
     */
    @GetMapping("/pendientes")
    public ResponseEntity<List<SolicitudUbicacionResponse>> obtenerSolicitudesPendientes() {
        String identificacion = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario empleado = usuarioService.obtenerPorIdentificacion(identificacion);

        List<SolicitudUbicacionResponse> pendientes = geolocalizacionService.obtenerSolicitudesPendientes(empleado);

        return ResponseEntity.ok(pendientes);
    }
}
