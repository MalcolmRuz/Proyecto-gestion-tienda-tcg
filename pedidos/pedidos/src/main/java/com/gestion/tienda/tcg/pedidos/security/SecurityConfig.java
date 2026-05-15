package com.gestion.tienda.tcg.pedidos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // SOLO ADMIN
                        .requestMatchers("/api/v1/pedidos/admin/**")
                        .hasRole("ADMIN")

                        // ADMIN Y CLIENTE
                        .requestMatchers("/api/v1/pedidos/**")
                        .hasAnyRole("ADMIN", "CLIENTE")

                        // OTROS
                        .anyRequest()
                        .authenticated())

                // temporal (hasta JWT)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}