package com.gestion.tienda.tcg.registro.dto;

import com.gestion.tienda.tcg.registro.enums.TipoDespacho;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor

//DTO utilizado para recibir los datos
// necesarios al registrar un usuario
public class UsuarioRequest {

    @NotBlank(message = "Debe ingresar un nombre")
    @Size(max = 40)
    private String nombre;

    @Email(message = "Debe ingresar un correo")
    @Size(max = 40)
    private String email;

    //Contraseña ingresada por usuario al registrar,
    // aún no se encuentra encriptada
    @NotBlank(message = "Debe ingresar una contraseña")
    @Size(max = 40)
    private String password;

    private String direccion;

    //Tipo de despacho selecionado de enums/TipoDespacho
    @NotNull(message = "Debe ingresar un tipo de despacho")
    private TipoDespacho tipoDespacho;

}
