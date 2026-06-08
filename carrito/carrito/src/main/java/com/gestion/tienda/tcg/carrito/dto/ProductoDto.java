package com.gestion.tienda.tcg.carrito.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDto {
    private Long idProducto;
    private String nombreProducto;
    private String descripcion;
    private Double precioUnitario;
}
