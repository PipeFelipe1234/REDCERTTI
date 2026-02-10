package com.practica.backend.service;

import com.practica.backend.dto.UsuarioRequest;
import com.practica.backend.dto.UsuarioResponse;
import com.practica.backend.dto.UsuarioUpdateRequest;
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
                usuario.setFoto(request.foto());
                usuario.setTelefono(request.telefono());
                usuario.setCargo(request.cargo());

                Usuario guardado = usuarioRepository.save(usuario);

                return new UsuarioResponse(
                                guardado.getId(),
                                guardado.getIdentificacion(),
                                guardado.getNombre(),
                                guardado.getEmail(),
                                guardado.getRol(),
                                guardado.getFoto(),
                                guardado.getTelefono(),
                                guardado.getCargo());
        }

        public UsuarioResponse actualizarUsuario(Long id, UsuarioRequest request) {
                Usuario usuario = usuarioRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                usuario.setIdentificacion(request.identificacion());
                usuario.setNombre(request.nombre());
                usuario.setEmail(request.email());
                usuario.setRol(request.rol());
                usuario.setFoto(request.foto());
                usuario.setTelefono(request.telefono());
                usuario.setCargo(request.cargo());

                Usuario actualizado = usuarioRepository.save(usuario);

                return new UsuarioResponse(
                                actualizado.getId(),
                                actualizado.getIdentificacion(),
                                actualizado.getNombre(),
                                actualizado.getEmail(),
                                actualizado.getRol(),
                                actualizado.getFoto(),
                                actualizado.getTelefono(),
                                actualizado.getCargo());
        }

        public Usuario obtenerPorId(Long id) {
                return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        }

        public UsuarioResponse obtenerUsuarioResponsePorId(Long id) {
                Usuario u = usuarioRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                return new UsuarioResponse(
                                u.getId(),
                                u.getIdentificacion(),
                                u.getNombre(),
                                u.getEmail(),
                                u.getRol(),
                                u.getFoto(),
                                u.getTelefono(),
                                u.getCargo());
        }

        public Usuario obtenerPorIdentificacion(String identificacion) {
                return usuarioRepository.findByIdentificacion(identificacion)
                                .orElse(null);
        }

        public UsuarioResponse obtenerUsuarioResponsePorIdentificacion(String identificacion) {
                Usuario u = usuarioRepository.findByIdentificacion(identificacion)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                return new UsuarioResponse(
                                u.getId(),
                                u.getIdentificacion(),
                                u.getNombre(),
                                u.getEmail(),
                                u.getRol(),
                                u.getFoto(),
                                u.getTelefono(),
                                u.getCargo());
        }

        public UsuarioResponse actualizarPorIdentificacion(String identificacion, UsuarioRequest request) {

                Usuario usuario = usuarioRepository.findByIdentificacion(identificacion)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                usuario.setNombre(request.nombre());
                usuario.setEmail(request.email());
                usuario.setRol(request.rol());
                usuario.setFoto(request.foto());
                usuario.setTelefono(request.telefono());
                usuario.setCargo(request.cargo());

                Usuario actualizado = usuarioRepository.save(usuario);

                return new UsuarioResponse(
                                actualizado.getId(),
                                actualizado.getIdentificacion(),
                                actualizado.getNombre(),
                                actualizado.getEmail(),
                                actualizado.getRol(),
                                actualizado.getFoto(),
                                actualizado.getTelefono(),
                                actualizado.getCargo());
        }

        public List<UsuarioResponse> obtenerTodos() {
                return usuarioRepository.findAll()
                                .stream()
                                .map(u -> new UsuarioResponse(
                                                u.getId(),
                                                u.getIdentificacion(),
                                                u.getNombre(),
                                                u.getEmail(),
                                                u.getRol(),
                                                u.getFoto(),
                                                u.getTelefono(),
                                                u.getCargo()))
                                .toList();
        }

        /**
         * 🔄 Actualizar usuario parcialmente por ID (ADMIN)
         * Solo actualiza los campos que se envían en el request (no nulos y no vacíos)
         */
        public UsuarioResponse actualizarUsuarioParcial(Long id, UsuarioUpdateRequest request) {
                Usuario usuario = usuarioRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

                // Solo actualizar si el campo viene con valor
                // 🆔 Actualizar identificación (validar que no exista otra igual)
                if (request.identificacion() != null && !request.identificacion().isBlank()) {
                        // Verificar que la nueva identificación no esté en uso por OTRO usuario
                        usuarioRepository.findByIdentificacion(request.identificacion())
                                        .ifPresent(existente -> {
                                                if (!existente.getId().equals(id)) {
                                                        throw new RuntimeException(
                                                                        "La identificación ya está en uso por otro usuario");
                                                }
                                        });
                        usuario.setIdentificacion(request.identificacion());
                }
                if (request.nombre() != null && !request.nombre().isBlank()) {
                        usuario.setNombre(request.nombre());
                }
                if (request.email() != null && !request.email().isBlank()) {
                        usuario.setEmail(request.email());
                }
                if (request.rol() != null && !request.rol().isBlank()) {
                        usuario.setRol(request.rol());
                }
                if (request.foto() != null) {
                        usuario.setFoto(request.foto());
                }
                if (request.telefono() != null && !request.telefono().isBlank()) {
                        usuario.setTelefono(request.telefono());
                }
                if (request.cargo() != null) {
                        usuario.setCargo(request.cargo());
                }

                Usuario actualizado = usuarioRepository.save(usuario);

                return new UsuarioResponse(
                                actualizado.getId(),
                                actualizado.getIdentificacion(),
                                actualizado.getNombre(),
                                actualizado.getEmail(),
                                actualizado.getRol(),
                                actualizado.getFoto(),
                                actualizado.getTelefono(),
                                actualizado.getCargo());
        }
}
