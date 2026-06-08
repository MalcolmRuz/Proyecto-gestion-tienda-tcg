package com.gestion.tienda.tcg.pedido.dto;

import java.time.LocalDateTime;

import com.gestion.tienda.tcg.pedido.enums.EstadoPedido;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistorialPedidoResponse {

    private Long idHistorial;

    private EstadoPedido estadoPedido;

    private String descripcion;

    private LocalDateTime fecha;
}