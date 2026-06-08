package com.gestion.tienda.tcg.registro.security.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

// DTO utilizado para retornar el token JWT
// luego de un inicio de sesión exitoso.
@Getter
@AllArgsConstructor
public class LoginResponse {

    // Token JWT generado para el usuario autenticado
    private String token;

}