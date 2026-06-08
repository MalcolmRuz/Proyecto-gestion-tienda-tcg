package com.gestion.tienda.tcg.registro.security.auth;

import lombok.Getter;
import lombok.Setter;

// DTO utilizado para recibir las credenciales
// de inicio de sesión del usuario.
@Getter
@Setter
public class LoginRequest {

    private String email;
    private String password;

}
