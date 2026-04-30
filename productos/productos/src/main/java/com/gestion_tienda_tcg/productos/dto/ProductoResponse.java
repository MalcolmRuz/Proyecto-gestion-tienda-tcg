package com.gestion_tienda_tcg.productos.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class ProductoResponse {
    private final Long idProducto;
    private final String nombreProducto;
    private final String descripcion;
    private final Boolean estadoActivo;
    private final Long idProveedor;
}
