package com.practica.backend.controller;

import com.practica.backend.service.RegistroService;
import com.practica.backend.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final RegistroService registroService;
    private final UsuarioService usuarioService;

    public AdminController(
            RegistroService registroService,
            UsuarioService usuarioService) {
        this.registroService = registroService;
        this.usuarioService = usuarioService;
    }

    // 👮 VER TODOS LOS REGISTROS
    @GetMapping("/registros")
    public ResponseEntity<?> todosLosRegistros() {
        return ResponseEntity.ok(registroService.obtenerTodos());
    }

    // 👮 VER TODOS LOS USUARIOS
    @GetMapping("/usuarios")
    public ResponseEntity<?> todosLosUsuarios() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }
}
