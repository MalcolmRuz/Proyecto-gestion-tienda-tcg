package com.gestion_tienda_tcg.productos.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProveedorRequest {
@NotBlank
    private String nombre;
@NotBlank
    private String contacto;

}
