package com.gestion.tienda.tcg.pedido.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.gestion.tienda.tcg.pedido.security.jwt.JwtAuthenticationFilter;
import com.gestion.tienda.tcg.pedido.security.jwt.JwtService;

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
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth

                                                // Swagger
                                                .requestMatchers(
                                                                "/swagger-ui/**",
                                                                "/v3/api-docs/**",
                                                                "/doc/swagger-ui.html")
                                                .permitAll()

                                                // Actuator (healthcheck de Docker)
                                                .requestMatchers(
                                                                "/actuator/health",
                                                                "/actuator/health/**")
                                                .permitAll()

                                                // =========================================================================
                                                // REGLAS PARA PEDIDOS
                                                // =========================================================================
                                                // Permitimos que el CLIENTE cree su propio pedido (POST) y consulte los
                                                // suyos (GET)
                                                .requestMatchers(HttpMethod.POST, "/api/v1/pedidos/**")
                                                .hasAnyRole("ADMIN", "CLIENTE")
                                                .requestMatchers(HttpMethod.GET, "/api/v1/pedidos/**")
                                                .hasAnyRole("ADMIN", "CLIENTE")

                                                // Modificaciones y eliminaciones solo para ADMIN
                                                .requestMatchers(HttpMethod.PUT, "/api/v1/pedidos/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/api/v1/pedidos/**")
                                                .hasRole("ADMIN")

                                                // =========================================================================
                                                // REGLAS PARA SUB-RECURSOS (DETALLES Y HISTORIAL)
                                                // =========================================================================
                                                // Visualización para ambos
                                                .requestMatchers(HttpMethod.GET, "/api/v1/detalles/**")
                                                .hasAnyRole("ADMIN", "CLIENTE")
                                                .requestMatchers(HttpMethod.GET, "/api/v1/historial/**")
                                                .hasAnyRole("ADMIN", "CLIENTE")

                                                // Alteraciones solo para ADMIN
                                                .requestMatchers("/api/v1/detalles/**").hasRole("ADMIN")
                                                .requestMatchers("/api/v1/historial/**").hasRole("ADMIN")

                                                // =========================================================================
                                                // CONTROL GLOBAL
                                                // =========================================================================
                                                .anyRequest().authenticated())
                                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                                .build();
        }
}