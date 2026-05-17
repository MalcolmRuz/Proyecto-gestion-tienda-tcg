package com.gestion.tienda.tcg.carrito.security;

import com.gestion.tienda.tcg.carrito.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http

                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // CAMBIAR MATCHERS

                        .requestMatchers("/api/v1/movimientosStock/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/carrito/*").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/carrito").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/carrito").hasRole("ADMIN")

                        .requestMatchers("/api/v1/carrito/*/aumentar/*").hasRole("ADMIN")
                        .requestMatchers("/api/v1/carrito/*/reducir/*").authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}