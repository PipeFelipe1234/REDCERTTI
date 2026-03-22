package com.practica.backend.repository;

import com.practica.backend.entity.SolicitudUbicacion;
import com.practica.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SolicitudUbicacionRepository extends JpaRepository<SolicitudUbicacion, Long> {

    // Buscar solicitudes pendientes de un empleado
    List<SolicitudUbicacion> findByEmpleadoAndEstado(Usuario empleado, String estado);

    // Buscar solicitudes por admin
    List<SolicitudUbicacion> findByAdminOrderByFechaSolicitudDesc(Usuario admin);

    // Buscar solicitud por ID y empleado (para validar que el empleado puede
    // responder)
    Optional<SolicitudUbicacion> findByIdAndEmpleado(Long id, Usuario empleado);

    // Buscar solicitudes pendientes que hayan expirado (más de X minutos)
    @Query("SELECT s FROM SolicitudUbicacion s WHERE s.estado = 'PENDIENTE' AND s.fechaSolicitud < :fechaLimite")
    List<SolicitudUbicacion> findSolicitudesExpiradas(@Param("fechaLimite") LocalDateTime fechaLimite);

    // Buscar última solicitud pendiente de un empleado
    @Query("SELECT s FROM SolicitudUbicacion s WHERE s.empleado = :empleado AND s.estado = 'PENDIENTE' ORDER BY s.fechaSolicitud DESC")
    List<SolicitudUbicacion> findSolicitudesPendientesByEmpleado(@Param("empleado") Usuario empleado);

    // ============================
    // 📊 HISTORIAL Y FILTROS
    // ============================

    // Obtener todas las solicitudes ordenadas por fecha descendente
    List<SolicitudUbicacion> findAllByOrderByFechaSolicitudDesc();

    // Obtener solicitudes por rango de fechas
    @Query("SELECT s FROM SolicitudUbicacion s WHERE DATE(s.fechaSolicitud) >= :fechaInicio AND DATE(s.fechaSolicitud) <= :fechaFin ORDER BY s.fechaSolicitud DESC")
    List<SolicitudUbicacion> findByFechaRange(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    // Obtener solicitudes de un empleado específico
    List<SolicitudUbicacion> findByEmpleadoOrderByFechaSolicitudDesc(Usuario empleado);

    // Obtener solicitudes por mes y año
    @Query("SELECT s FROM SolicitudUbicacion s WHERE MONTH(s.fechaSolicitud) = :mes AND YEAR(s.fechaSolicitud) = :anio ORDER BY s.fechaSolicitud DESC")
    List<SolicitudUbicacion> findByMesYAnio(@Param("mes") int mes, @Param("anio") int anio);

    // ============================
    // 🗑️ LIMPIEZA AUTOMÁTICA
    // ============================

    // Contar solicitudes de un mes y año específico
    @Query("SELECT COUNT(s) FROM SolicitudUbicacion s WHERE MONTH(s.fechaSolicitud) = :mes AND YEAR(s.fechaSolicitud) = :anio")
    long countByMesYAnio(@Param("mes") int mes, @Param("anio") int anio);

    // Eliminar solicitudes de un mes y año específico
    @Modifying
    @Transactional
    @Query("DELETE FROM SolicitudUbicacion s WHERE MONTH(s.fechaSolicitud) = :mes AND YEAR(s.fechaSolicitud) = :anio")
    int deleteByMesYAnio(@Param("mes") int mes, @Param("anio") int anio);

    // ============================
    // 📅 MESES DISPONIBLES
    // ============================

    // Obtener el mes más antiguo con solicitudes
    @Query("SELECT MIN(s.fechaSolicitud) FROM SolicitudUbicacion s")
    LocalDateTime findFechaMasAntigua();

    // Obtener el mes más reciente con solicitudes
    @Query("SELECT MAX(s.fechaSolicitud) FROM SolicitudUbicacion s")
    LocalDateTime findFechaMasReciente();
}
