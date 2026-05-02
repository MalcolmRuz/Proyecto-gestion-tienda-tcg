package com.gestion.tienda.tcg.pedidos.dto;

import com.gestion.tienda.tcg.pedidos.enums.EstadoPedido;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HistorialPedidoRequest {

    @NotNull(message = "El estado es obligatorio")
    private EstadoPedido estadoPedido;
}