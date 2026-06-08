package com.gestion.tienda.tcg.carrito.security;

import com.gestion.tienda.tcg.carrito.security.jwt.JwtAuthenticationFilter;
import com.gestion.tienda.tcg.carrito.security.jwt.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtService jwtService;

    public SecurityConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        //CrearCarrito, Agregar, Actualizar, Borrar items, Pagar y Cancelar
                        //Accesible por cualquier usuario autenticado (ADMIN o CLIENTE)
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/carritos/**"
                        ).authenticated()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/carritos/**"
                        ).authenticated()
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/v1/carritos/**"
                        ).authenticated()
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/v1/carritos/**"
                        ).authenticated()
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/v1/carritos/**"
                        ).authenticated()

                        //Movimientos de Stock
                        .requestMatchers(
                                "/api/v1/movimientosStock/**"
                        ).hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}