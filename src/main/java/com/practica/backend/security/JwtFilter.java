package com.practica.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String requestUri = request.getRequestURI();

        // Si tiene Authorization header, intentar validar el token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                // Validar que el token sea válido y no expirado
                if (JwtUtil.validarToken(token)) {
                    // Token válido - extraer información
                    String identificacion = JwtUtil.extraerIdentificacion(token);
                    String rol = JwtUtil.extraerRol(token);

                    // Crear lista de autoridades
                    List<SimpleGrantedAuthority> autoridades = new ArrayList<>();
                    if (rol != null && !rol.isEmpty()) {
                        autoridades.add(new SimpleGrantedAuthority("ROLE_" + rol));
                    }

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            identificacion,
                            null,
                            autoridades);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                // Si el token es inválido o expirado, simplemente no autentica
                // Spring Security decidirá si requiere autenticación basado en la configuración
            } catch (Exception e) {
                // Error al procesar token, no establecer autenticación
            }
        }

        filterChain.doFilter(request, response);
    }
}
