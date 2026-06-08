package com.gestion.tienda.tcg.pedido.dto;

import java.time.LocalDateTime;

import com.gestion.tienda.tcg.pedido.enums.EstadoPedido;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HistorialPedidoResponse {
    private EstadoPedido estadoPedido;
    private String descripcion;
    private LocalDateTime fechaCambio;
}