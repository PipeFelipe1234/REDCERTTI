package com.practica.backend.service;

import com.practica.backend.dto.UsuarioRequest;
import com.practica.backend.dto.UsuarioResponse;
import com.practica.backend.entity.Usuario;
import com.practica.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioResponse crearUsuario(UsuarioRequest request) {

        if (usuarioRepository.findByIdentificacion(request.identificacion()).isPresent()) {
            throw new RuntimeException("La identificacion ya esta registrada");
        }

        Usuario usuario = new Usuario();
        usuario.setIdentificacion(request.identificacion());
        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());
        usuario.setRol(request.rol());

        Usuario guardado = usuarioRepository.save(usuario);

        return new UsuarioResponse(
                guardado.getId(),
                guardado.getIdentificacion(),
                guardado.getNombre(),
                guardado.getEmail(),
                guardado.getRol());
    }

    public UsuarioResponse actualizarUsuario(Long id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setIdentificacion(request.identificacion());
        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());
        usuario.setRol(request.rol());

        Usuario actualizado = usuarioRepository.save(usuario);

        return new UsuarioResponse(
                actualizado.getId(),
                actualizado.getIdentificacion(),
                actualizado.getNombre(),
                actualizado.getEmail(),
                actualizado.getRol());
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario obtenerPorIdentificacion(String identificacion) {
        return usuarioRepository.findByIdentificacion(identificacion)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public UsuarioResponse actualizarPorIdentificacion(String identificacion, UsuarioRequest request) {

        Usuario usuario = usuarioRepository.findByIdentificacion(identificacion)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());
        usuario.setRol(request.rol());

        Usuario actualizado = usuarioRepository.save(usuario);

        return new UsuarioResponse(
                actualizado.getId(),
                actualizado.getIdentificacion(),
                actualizado.getNombre(),
                actualizado.getEmail(),
                actualizado.getRol());
    }

    public List<UsuarioResponse> obtenerTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(u -> new UsuarioResponse(
                        u.getId(),
                        u.getIdentificacion(),
                        u.getNombre(),
                        u.getEmail(),
                        u.getRol()))
                .toList();
    }
}
