package com.gestion.tienda.tcg.pedidos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DetallePedidoResponse {

    private Long idBoleta;

    private Long productoId;

    private Integer cantidad;

    private Double precioUnitario;

    private Double precioTotal;
}