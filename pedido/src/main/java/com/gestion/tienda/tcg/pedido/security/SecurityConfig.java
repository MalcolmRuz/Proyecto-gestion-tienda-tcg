package com.gestion.tienda.tcg.pedido.security;

import com.gestion.tienda.tcg.pedido.security.jwt.JwtAuthenticationFilter;
import com.gestion.tienda.tcg.pedido.security.jwt.JwtService;
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

                                // =========================================================================
                                // REGLAS PARA PEDIDOS
                                // =========================================================================

                                // Solo visualizaci贸n (GET) permitida para ambos roles
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/pedidos/**"
                                ).hasAnyRole("ADMIN", "CLIENTE")

                                // Cualquier creaci贸n o modificaci贸n (POST, PUT, DELETE) queda restringida a ADMIN
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/v1/pedidos/**"
                                ).hasRole("ADMIN")
                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/v1/pedidos/**"
                                ).hasRole("ADMIN")
                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/api/v1/pedidos/**"
                                ).hasRole("ADMIN")

                                // =========================================================================
                                // REGLAS PARA SUB-RECURSOS (DETALLES, HISTORIAL, ENVIOS)
                                // =========================================================================

                                // Solo visualizaci贸n (GET) para CLIENTE y ADMIN
                                .requestMatchers(HttpMethod.GET, "/api/v1/detalles/**").hasAnyRole("ADMIN", "CLIENTE")
                                .requestMatchers(HttpMethod.GET, "/api/v1/historial/**").hasAnyRole("ADMIN", "CLIENTE")
                                .requestMatchers(HttpMethod.GET, "/api/v1/envios/**").hasAnyRole("ADMIN", "CLIENTE")

                                // Las alteraciones en los sub-recursos quedan bloqueadas para el CLIENTE, solo ADMIN
                                .requestMatchers("/api/v1/detalles/**").hasRole("ADMIN")
                                .requestMatchers("/api/v1/historial/**").hasRole("ADMIN")
                                .requestMatchers("/api/v1/envios/**").hasRole("ADMIN")

                                // =========================================================================
                                // CONTROL GLOBAL
                                // =========================================================================
                                .anyRequest().authenticated()
                        )
                        .addFilterBefore(jwtAuthenticationFilter(),
                                UsernamePasswordAuthenticationFilter.class)
                        .build();
        }
}