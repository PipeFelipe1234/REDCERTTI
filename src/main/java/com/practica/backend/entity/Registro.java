package com.practica.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "registros")
public class Registro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalTime horaEntrada;
    private LocalTime horaSalida;

    // 📍 GPS
    private Double latitud;
    private Double longitud;
    private Double precisionMetros;

    // 📝 Reporte del día
    @Column(columnDefinition = "TEXT")
    private String reporte;

    public Registro() {
    }

    public Registro(Long id, Usuario usuario, LocalDate fecha, LocalTime horaEntrada, LocalTime horaSalida,
            Double latitud, Double longitud, Double precisionMetros, String reporte) {
        this.id = id;
        this.usuario = usuario;
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.latitud = latitud;
        this.longitud = longitud;
        this.precisionMetros = precisionMetros;
        this.reporte = reporte;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getPrecisionMetros() {
        return precisionMetros;
    }

    public void setPrecisionMetros(Double precisionMetros) {
        this.precisionMetros = precisionMetros;
    }

    public String getReporte() {
        return reporte;
    }

    public void setReporte(String reporte) {
        this.reporte = reporte;
    }
}
