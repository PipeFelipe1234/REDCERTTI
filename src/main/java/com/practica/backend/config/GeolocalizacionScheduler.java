package com.practica.backend.config;

import com.practica.backend.service.GeolocalizacionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler para tareas programadas del sistema
 * - Expirar solicitudes de ubicación que no fueron respondidas en 90 segundos
 */
@Component
@EnableScheduling
public class GeolocalizacionScheduler {

    private static final Logger logger = LoggerFactory.getLogger(GeolocalizacionScheduler.class);

    private final GeolocalizacionService geolocalizacionService;

    public GeolocalizacionScheduler(GeolocalizacionService geolocalizacionService) {
        this.geolocalizacionService = geolocalizacionService;
    }

    /**
     * Ejecuta cada 30 segundos para revisar y expirar solicitudes pendientes
     * que superen los 90 segundos sin respuesta.
     * 
     * El admin recibirá notificación cuando una solicitud expire.
     */
    @Scheduled(fixedRate = 30000) // cada 30 segundos
    public void revisarSolicitudesExpiradas() {
        try {
            int expiradas = geolocalizacionService.expirarSolicitudesAntiguas();
            if (expiradas > 0) {
                logger.info("⏰ Scheduler: {} solicitudes expiradas", expiradas);
            }
        } catch (Exception e) {
            logger.error("❌ Error en scheduler de expiración: {}", e.getMessage());
        }
    }
}
