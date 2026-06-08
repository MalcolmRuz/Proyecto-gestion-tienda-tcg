package com.gestion.tienda.tcg.registro.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

//Servicio encargado de generar y validar tokens JWT
@Service
public class JwtService {

    // Clave secreta utilizada para firmar los tokens
    private final String SECRET_KEY =
            "bXlfY2xhdmVfc3VwZXJfc2VjcmV0YV9wYXJhX2p3dF8xMjM0NTY3ODkw";

    // Genera un token JWT con email y rol del usuario
    public String generateToken(String email, String rol){

        return Jwts.builder()
                // Usuario propietario del token
                .setSubject(email)
                // Rol agregado como claim personalizado
                .claim("rol", rol)
                // Fecha de creación del token
                .setIssuedAt(new Date())
                // Fecha de expiración (1 hora)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                // Firma del token usando algoritmo HS256
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();

    }
    // Extrae los claims (datos) almacenados dentro del token
    public Claims extractClaims(String token){

        return Jwts.parserBuilder()
                // Valida firma del token
                .setSigningKey(getSignKey())
                .build()
                // Valida el JWT y obtiene la información almacenada en él
                .parseClaimsJws(token)
                .getBody();

    }
    // Genera la clave de firma a partir de la SECRET_KEY
    private Key getSignKey(){

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

        return Keys.hmacShaKeyFor(keyBytes);

    }

}
