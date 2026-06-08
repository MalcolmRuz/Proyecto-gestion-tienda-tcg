package com.gestion.tienda.tcg.pedido.security.jwt;

import java.io.IOException;
import java.util.Collections;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // =========================
        // VALIDAR HEADER
        // =========================
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        try {

            // =========================
            // EXTRAER TOKEN
            // =========================
            String jwt = authHeader.substring(7);

            // =========================
            // EXTRAER CLAIMS
            // =========================
            Claims claims = jwtService.extractClaims(jwt);

            String email = claims.getSubject();

            String rol = claims.get("rol", String.class);

            // =========================
            // VALIDAR AUTENTICACION
            // =========================
            if (email != null
                    && SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {

                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
                        "ROLE_" + rol);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        Collections.singletonList(authority));

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request));

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);
            }

        } catch (JwtException ex) {

            // =========================
            // TOKEN INVALIDO
            // =========================
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            response.getWriter().write("Token JWT invalido");

            return;
        }

        filterChain.doFilter(request, response);
    }
}