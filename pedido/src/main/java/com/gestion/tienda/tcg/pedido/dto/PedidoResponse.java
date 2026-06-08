package com.gestion.tienda.tcg.pedido.dto;

import java.util.List;

import com.gestion.tienda.tcg.pedido.enums.EstadoEnvio;
import com.gestion.tienda.tcg.pedido.enums.EstadoPedido;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PedidoResponse {
    private Long id;
    private Long usuarioId;
    private Double totalPedido;
    private String direccionEnvio;
    private EstadoPedido estado;
    //private EstadoEnvio estadoEnvio;
    private List<DetallePedidoResponse> detalles;
}