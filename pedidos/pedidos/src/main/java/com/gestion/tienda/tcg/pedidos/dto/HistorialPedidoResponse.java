package com.gestion.tienda.tcg.pedidos.dto;

import java.time.LocalDateTime;

import com.gestion.tienda.tcg.pedidos.enums.EstadoPedido;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HistorialPedidoResponse {

    private Long idHistorial;

    private EstadoPedido estadoPedido;

    private LocalDateTime fecha;
}
