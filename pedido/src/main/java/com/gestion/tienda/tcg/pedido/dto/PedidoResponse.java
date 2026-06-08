package com.gestion.tienda.tcg.pedido.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.gestion.tienda.tcg.pedido.enums.EstadoPedido;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoResponse {

    private Long idPedido;

    private Long usuarioId;

    private Long carritoId;

    private LocalDateTime fecha;

    private EstadoPedido estado;

    private Double totalPedido;

    private List<DetallePedidoResponse> detalles;

    private List<HistorialPedidoResponse> historial;

    private EnvioResponse envio;
}