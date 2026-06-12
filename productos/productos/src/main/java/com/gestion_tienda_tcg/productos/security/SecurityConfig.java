package com.gestion_tienda_tcg.productos.security;

import com.gestion_tienda_tcg.productos.security.jwt.JwtAuthenticationFilter;
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

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        .requestMatchers("/api/v1/proveedor/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/categoria/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/productoCategoria/**").permitAll()

                        .requestMatchers("/api/v1/categoria/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/productoCategoria/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/productos/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/productos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/productos/**").hasRole("ADMIN")


                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
