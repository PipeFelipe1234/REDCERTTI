package com.practica.backend.service;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.practica.backend.dto.ResponderUbicacionRequest;
import com.practica.backend.dto.SolicitudUbicacionResponse;
import com.practica.backend.entity.SolicitudUbicacion;
import com.practica.backend.entity.TokenDispositivo;
import com.practica.backend.entity.Usuario;
import com.practica.backend.repository.SolicitudUbicacionRepository;
import com.practica.backend.repository.TokenDispositivoRepository;
import com.practica.backend.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GeolocalizacionService {

    private static final Logger logger = LoggerFactory.getLogger(GeolocalizacionService.class);
    private static final int SEGUNDOS_EXPIRACION = 90; // 90 segundos para expiración

    private final SolicitudUbicacionRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final TokenDispositivoRepository tokenDispositivoRepository;

    public GeolocalizacionService(
            SolicitudUbicacionRepository solicitudRepository,
            UsuarioRepository usuarioRepository,
            TokenDispositivoRepository tokenDispositivoRepository) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
        this.tokenDispositivoRepository = tokenDispositivoRepository;
    }

    /**
     * Admin solicita la ubicación de un empleado
     * 1. Crea registro en solicitudes_ubicacion con estado PENDIENTE
     * 2. Envía notificación silenciosa (data-only) al empleado
     * 3. Retorna el solicitudId
     */
    @Transactional
    public SolicitudUbicacionResponse solicitarUbicacion(Usuario admin, Long empleadoId) {
        logger.info("📍 Admin {} solicita ubicación del empleado ID: {}", admin.getNombre(), empleadoId);

        // Buscar el empleado
        Usuario empleado = usuarioRepository.findById(empleadoId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + empleadoId));

        // Validar que no sea el mismo usuario
        if (admin.getId().equals(empleadoId)) {
            throw new RuntimeException("No puedes solicitar tu propia ubicación");
        }

        // Crear la solicitud
        SolicitudUbicacion solicitud = new SolicitudUbicacion(admin, empleado);
        solicitud = solicitudRepository.save(solicitud);

        logger.info("✅ Solicitud creada con ID: {}", solicitud.getId());

        // Enviar notificación silenciosa al empleado
        boolean notificacionEnviada = enviarNotificacionSilenciosa(empleado, solicitud.getId());

        String mensaje = notificacionEnviada
                ? "Solicitud enviada. El empleado tiene " + SEGUNDOS_EXPIRACION + " segundos para responder."
                : "Solicitud creada, pero el empleado no tiene dispositivos registrados";

        return new SolicitudUbicacionResponse(
                solicitud.getId(),
                solicitud.getEstado(),
                mensaje);
    }

    /**
     * Empleado responde a una solicitud de ubicación
     * - Si todo bien: guarda coordenadas y notifica al admin
     * - Si hay error: guarda el error y notifica al admin del problema
     */
    @Transactional
    public void responderSolicitud(Usuario empleado, ResponderUbicacionRequest request) {
        logger.info("📍 Empleado {} responde solicitud ID: {}", empleado.getNombre(), request.solicitudId());

        // Buscar la solicitud y validar que pertenece al empleado
        SolicitudUbicacion solicitud = solicitudRepository.findByIdAndEmpleado(request.solicitudId(), empleado)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada o no te pertenece"));

        // Validar que esté pendiente
        if (!"PENDIENTE".equals(solicitud.getEstado())) {
            throw new RuntimeException("Esta solicitud ya fue respondida o expiró");
        }

        solicitud.setFechaRespuesta(LocalDateTime.now());

        // Verificar si es una respuesta con error (GPS desactivado, permiso denegado,
        // etc.)
        if (Boolean.TRUE.equals(request.error())) {
            solicitud.setEstado("ERROR");
            solicitud.setMensajeError(request.mensajeError() != null ? request.mensajeError() : "Error desconocido");
            solicitudRepository.save(solicitud);

            logger.warn("⚠️ Solicitud {} respondida con ERROR: {}", request.solicitudId(), request.mensajeError());

            // Notificar al admin del error
            enviarNotificacionAlAdmin(
                    solicitud.getAdmin(),
                    "Error al obtener ubicación",
                    "El empleado " + empleado.getNombre() + " reportó: " + solicitud.getMensajeError(),
                    "UBICACION_ERROR",
                    solicitud.getId());
        } else {
            // Respuesta exitosa con coordenadas
            solicitud.setLatitud(request.latitud());
            solicitud.setLongitud(request.longitud());
            solicitud.setPrecisionMetros(request.precisionMetros());
            solicitud.setUbicacion(request.ubicacion());
            solicitud.setEstado("RESPONDIDA");
            solicitudRepository.save(solicitud);

            logger.info("✅ Solicitud {} respondida exitosamente", request.solicitudId());

            // Notificar al admin que ya tiene la ubicación
            String ubicacionTexto = request.ubicacion() != null
                    ? request.ubicacion()
                    : String.format("Lat: %.6f, Lon: %.6f", request.latitud(), request.longitud());

            enviarNotificacionAlAdmin(
                    solicitud.getAdmin(),
                    "Ubicación recibida",
                    "Empleado " + empleado.getNombre() + ": " + ubicacionTexto,
                    "UBICACION_RECIBIDA",
                    solicitud.getId());
        }
    }

    /**
     * Admin consulta el resultado de una solicitud (polling)
     */
    public SolicitudUbicacionResponse obtenerResultado(Long solicitudId) {
        SolicitudUbicacion solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + solicitudId));

        if ("ERROR".equals(solicitud.getEstado())) {
            return new SolicitudUbicacionResponse(
                    solicitud.getId(),
                    solicitud.getEstado(),
                    solicitud.getEmpleado().getId(),
                    solicitud.getEmpleado().getNombre(),
                    solicitud.getFechaSolicitud(),
                    solicitud.getFechaRespuesta(),
                    solicitud.getMensajeError());
        }

        return new SolicitudUbicacionResponse(
                solicitud.getId(),
                solicitud.getEstado(),
                solicitud.getEmpleado().getId(),
                solicitud.getEmpleado().getNombre(),
                solicitud.getLatitud(),
                solicitud.getLongitud(),
                solicitud.getPrecisionMetros(),
                solicitud.getUbicacion(),
                solicitud.getFechaSolicitud(),
                solicitud.getFechaRespuesta());
    }

    /**
     * Obtener solicitudes pendientes de un empleado
     * (para que la app sepa si tiene solicitudes por responder)
     */
    public List<SolicitudUbicacionResponse> obtenerSolicitudesPendientes(Usuario empleado) {
        return solicitudRepository.findSolicitudesPendientesByEmpleado(empleado)
                .stream()
                .map(s -> new SolicitudUbicacionResponse(
                        s.getId(),
                        s.getEstado(),
                        s.getEmpleado().getId(),
                        s.getEmpleado().getNombre(),
                        null, null, null, null,
                        s.getFechaSolicitud(),
                        null))
                .toList();
    }

    /**
     * Envía notificación SILENCIOSA (data-only, sin título ni body)
     * al dispositivo del empleado
     */
    private boolean enviarNotificacionSilenciosa(Usuario empleado, Long solicitudId) {
        try {
            List<TokenDispositivo> tokens = tokenDispositivoRepository.findTokensActivosByUsuario(empleado);

            if (tokens.isEmpty()) {
                logger.warn("⚠️ El empleado {} no tiene dispositivos registrados", empleado.getNombre());
                return false;
            }

            for (TokenDispositivo tokenDispositivo : tokens) {
                String fcmToken = tokenDispositivo.getToken();

                // Crear mensaje DATA-ONLY (sin .setNotification())
                Message message = Message.builder()
                        .setToken(fcmToken)
                        .putData("type", "SOLICITUD_UBICACION")
                        .putData("solicitudId", String.valueOf(solicitudId))
                        .putData("timeout", String.valueOf(SEGUNDOS_EXPIRACION))
                        // NO poner .setNotification() — esto la hace silenciosa
                        .setAndroidConfig(AndroidConfig.builder()
                                .setPriority(AndroidConfig.Priority.HIGH)
                                .build())
                        .build();

                String messageId = FirebaseMessaging.getInstance().send(message);
                logger.info("✅ Notificación silenciosa enviada a {}: {}", empleado.getNombre(), messageId);
            }

            return true;
        } catch (Exception e) {
            logger.error("❌ Error al enviar notificación silenciosa: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Envía notificación VISIBLE al admin
     * (con título y body - se muestra en el dispositivo)
     */
    private void enviarNotificacionAlAdmin(Usuario admin, String titulo, String cuerpo, String tipo, Long solicitudId) {
        try {
            List<TokenDispositivo> tokens = tokenDispositivoRepository.findTokensActivosByUsuario(admin);

            if (tokens.isEmpty()) {
                logger.warn("⚠️ El admin {} no tiene dispositivos registrados", admin.getNombre());
                return;
            }

            for (TokenDispositivo tokenDispositivo : tokens) {
                String fcmToken = tokenDispositivo.getToken();

                Message message = Message.builder()
                        .setToken(fcmToken)
                        .setNotification(Notification.builder()
                                .setTitle(titulo)
                                .setBody(cuerpo)
                                .build())
                        .putData("type", tipo)
                        .putData("solicitudId", String.valueOf(solicitudId))
                        .setAndroidConfig(AndroidConfig.builder()
                                .setPriority(AndroidConfig.Priority.HIGH)
                                .build())
                        .build();

                String messageId = FirebaseMessaging.getInstance().send(message);
                logger.info("✅ Notificación al admin {} enviada: {}", admin.getNombre(), messageId);
            }
        } catch (Exception e) {
            logger.error("❌ Error al enviar notificación al admin: {}", e.getMessage());
        }
    }

    /**
     * Marcar solicitudes antiguas como expiradas
     * Expira solicitudes PENDIENTES de más de 90 segundos
     * Notifica al admin de cada expiración
     */
    @Transactional
    public int expirarSolicitudesAntiguas() {
        LocalDateTime fechaLimite = LocalDateTime.now().minusSeconds(SEGUNDOS_EXPIRACION);
        List<SolicitudUbicacion> expiradas = solicitudRepository.findSolicitudesExpiradas(fechaLimite);

        for (SolicitudUbicacion solicitud : expiradas) {
            solicitud.setEstado("EXPIRADA");
            solicitud.setFechaRespuesta(LocalDateTime.now());
            solicitudRepository.save(solicitud);

            // Notificar al admin que expiró
            enviarNotificacionAlAdmin(
                    solicitud.getAdmin(),
                    "Solicitud expirada",
                    "El empleado " + solicitud.getEmpleado().getNombre() + " no respondió en " + SEGUNDOS_EXPIRACION
                            + " segundos",
                    "UBICACION_EXPIRADA",
                    solicitud.getId());
        }

        if (!expiradas.isEmpty()) {
            logger.info("⏰ {} solicitudes marcadas como expiradas", expiradas.size());
        }

        return expiradas.size();
    }
}
