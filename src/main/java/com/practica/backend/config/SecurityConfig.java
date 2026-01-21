package com.practica.backend.config;

import com.practica.backend.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // PUBLICOS
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/usuarios").permitAll()

                        // USER
                        .requestMatchers("/api/registros/mis-registros").hasRole("USER")
                        .requestMatchers("/api/registros/entrada").hasRole("USER")
                        .requestMatchers("/api/registros/salida").hasRole("USER")

                        // ADMIN
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // TODO LO DEMÁS

                        .anyRequest().authenticated())
                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
