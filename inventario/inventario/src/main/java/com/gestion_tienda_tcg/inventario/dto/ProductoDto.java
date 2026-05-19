package com.gestion_tienda_tcg.inventario.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoDto {
    private Long idProducto;
    private String nombreProducto;
    private String descripcion;
    private int precio;
    private boolean activo;

}
