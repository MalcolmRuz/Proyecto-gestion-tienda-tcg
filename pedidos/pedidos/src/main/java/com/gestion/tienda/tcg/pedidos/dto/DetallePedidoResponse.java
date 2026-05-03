package com.gestion.tienda.tcg.pedidos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetallePedidoResponse {

    private Long idDetalle;

    private Long productoId;

    private String nombreProducto;

    private Integer cantidad;

    private Double precioUnitario;

    private Double precioTotal;
}