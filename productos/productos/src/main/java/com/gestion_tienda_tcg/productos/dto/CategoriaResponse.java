package com.gestion_tienda_tcg.productos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class CategoriaResponse {
    private final Long idCategoria;
    private final String nombre;
    private final String descripcion;


}
