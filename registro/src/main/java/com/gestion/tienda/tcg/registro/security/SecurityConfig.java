package com.gestion.tienda.tcg.registro.security;

import com.gestion.tienda.tcg.registro.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;

    }

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // Swagger
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/doc/swagger-ui.html"
                        )
                        .permitAll()

                        // login
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/usuarios/auth/**",
                                "/auth/**"
                        )
                        .permitAll()

                        // registrar usuario
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/usuarios"
                        )
                        .permitAll()

                        // buscar por id
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/usuarios/*"
                        )
                        .hasRole("ADMIN")

                        // listar
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/usuarios"
                        )
                        .hasRole("ADMIN")

                        // actualizar
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/v1/usuarios/**"
                        )
                        .hasRole("ADMIN")

                        // eliminar
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/v1/usuarios/**"
                        )
                        .hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();

    }

}