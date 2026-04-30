package com.gestion_tienda_tcg.productos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProveedorResponse {
    private final Long idProveedor;
    private final String nombre;
    private final String contacto;



}
