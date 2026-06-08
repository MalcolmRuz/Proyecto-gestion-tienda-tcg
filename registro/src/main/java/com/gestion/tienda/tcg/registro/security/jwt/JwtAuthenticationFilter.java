package com.gestion.tienda.tcg.registro.security.jwt;

import com.gestion.tienda.tcg.registro.security.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Filtro encargado de validar el token JWT
// en cada petición HTTP
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Servicio JWT para validar y extraer datos del token
    private final JwtService jwtService;

    // Servicio encargado de cargar usuarios desde la base de datos
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService
    ) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;

    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Obtiene encabezado Authorization
        final String authHeader = request.getHeader("Authorization");

        final String jwt;

        final String email;

        // Verifica si existe token y si comienza con "Bearer "
        if(authHeader == null || !authHeader.startsWith("Bearer ")){

            filterChain.doFilter(request, response);

            return;

        }

        // Extrae token JWT eliminando "Bearer "
        jwt = authHeader.substring(7);

        // Extrae claims almacenados en el token
        Claims claims = jwtService.extractClaims(jwt);

        // Obtiene email almacenado como subject
        email = claims.getSubject();

        // Verifica si el usuario aún no está autenticado
        if(email != null
                && SecurityContextHolder.getContext().getAuthentication() == null){

            // Carga usuario desde base de datos
            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(email);

            // Crea autenticación con usuario y roles
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
            // Agrega detalles de la petición HTTP
            authToken.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

            // Guarda autenticación en el contexto de seguridad
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authToken);

        }
        // Continúa con la cadena de filtros
        filterChain.doFilter(request, response);

    }

}