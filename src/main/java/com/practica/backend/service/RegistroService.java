package com.practica.backend.service;

import com.practica.backend.dto.MarcarSalidaRequest;
import com.practica.backend.dto.RegistroResponse;
import com.practica.backend.entity.Registro;
import com.practica.backend.entity.Usuario;
import com.practica.backend.repository.RegistroRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class RegistroService {

    private final RegistroRepository registroRepository;

    public RegistroService(RegistroRepository registroRepository) {
        this.registroRepository = registroRepository;
    }

    public RegistroResponse marcarEntrada(Usuario usuario) {

        LocalDate hoy = LocalDate.now();

        if (registroRepository.findByUsuarioAndFecha(usuario, hoy).isPresent()) {
            throw new RuntimeException("Ya marcó asistencia hoy");
        }

        Registro registro = new Registro();
        registro.setUsuario(usuario);
        registro.setFecha(hoy);
        registro.setHoraEntrada(LocalTime.now());

        Registro guardado = registroRepository.save(registro);

        return mapToResponse(guardado);
    }

    public RegistroResponse marcarSalida(
            Usuario usuario,
            MarcarSalidaRequest request) {

        Registro registro = registroRepository
                .findByUsuarioAndFecha(usuario, LocalDate.now())
                .orElseThrow(() -> new RuntimeException("No hay entrada registrada"));

        if (request.precisionMetros() > 50) {
            throw new RuntimeException("Precisión GPS insuficiente");
        }

        registro.setHoraSalida(LocalTime.now());
        registro.setLatitud(request.latitud());
        registro.setLongitud(request.longitud());
        registro.setPrecisionMetros(request.precisionMetros());
        registro.setReporte(request.reporte());

        Registro guardado = registroRepository.save(registro);

        return mapToResponse(guardado);
    }

    public List<RegistroResponse> obtenerMisRegistros(Usuario usuario) {
        return registroRepository.findAllByUsuario(usuario)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<RegistroResponse> obtenerTodos() {
        return registroRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // 🔁 Mapper centralizado
    private RegistroResponse mapToResponse(Registro r) {
        return new RegistroResponse(
                r.getId(),
                r.getFecha(),
                r.getHoraEntrada(),
                r.getHoraSalida(),
                r.getLatitud(),
                r.getLongitud(),
                r.getPrecisionMetros(),
                r.getReporte(),
                r.getUsuario().getIdentificacion());

        // HOLA CAMBIO
    }
}
