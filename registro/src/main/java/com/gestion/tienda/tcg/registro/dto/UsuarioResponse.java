package com.gestion.tienda.tcg.registro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

// DTO utilizado para retornar la información
// de un usuario en las respuestas de la API
public class UsuarioResponse {


    private final Long idUsuario;
    private final String nombre;
    private final String email;
    private final String direccion;
    private final String nombreRol;
    private final String tipoDespacho;


}
