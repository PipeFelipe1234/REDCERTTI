package com.practica.backend.repository;

import com.practica.backend.entity.SolicitudUbicacion;
import com.practica.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SolicitudUbicacionRepository extends JpaRepository<SolicitudUbicacion, Long> {

    // Buscar solicitudes pendientes de un empleado
    List<SolicitudUbicacion> findByEmpleadoAndEstado(Usuario empleado, String estado);

    // Buscar solicitudes por admin
    List<SolicitudUbicacion> findByAdminOrderByFechaSolicitudDesc(Usuario admin);

    // Buscar solicitud por ID y empleado (para validar que el empleado puede responder)
    Optional<SolicitudUbicacion> findByIdAndEmpleado(Long id, Usuario empleado);

    // Buscar solicitudes pendientes que hayan expirado (más de X minutos)
    @Query("SELECT s FROM SolicitudUbicacion s WHERE s.estado = 'PENDIENTE' AND s.fechaSolicitud < :fechaLimite")
    List<SolicitudUbicacion> findSolicitudesExpiradas(@Param("fechaLimite") java.time.LocalDateTime fechaLimite);

    // Buscar última solicitud pendiente de un empleado
    @Query("SELECT s FROM SolicitudUbicacion s WHERE s.empleado = :empleado AND s.estado = 'PENDIENTE' ORDER BY s.fechaSolicitud DESC")
    List<SolicitudUbicacion> findSolicitudesPendientesByEmpleado(@Param("empleado") Usuario empleado);
}
