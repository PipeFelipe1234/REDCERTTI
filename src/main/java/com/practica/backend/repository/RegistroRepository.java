package com.practica.backend.repository;

import com.practica.backend.entity.Registro;
import com.practica.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface RegistroRepository extends JpaRepository<Registro, Long> {

    Optional<Registro> findByUsuarioAndFecha(Usuario usuario, LocalDate fecha);

    List<Registro> findAllByUsuario(Usuario usuario);
}
