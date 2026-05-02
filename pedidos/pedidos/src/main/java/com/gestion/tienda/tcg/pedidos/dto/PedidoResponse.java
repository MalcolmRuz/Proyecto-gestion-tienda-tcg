package com.gestion.tienda.tcg.pedidos.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.gestion.tienda.tcg.pedidos.enums.EstadoPedido;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PedidoResponse {

    private Long idPedido;

    private Long usuarioId;

    private LocalDateTime fecha;

    private EstadoPedido estado;

    private Double total;

    private List<DetallePedidoResponse> detalles;

    private EnvioResponse envio;

}
