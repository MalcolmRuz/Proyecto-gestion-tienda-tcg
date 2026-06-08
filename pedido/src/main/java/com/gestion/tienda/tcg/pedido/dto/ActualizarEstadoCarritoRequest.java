package com.gestion.tienda.tcg.pedido.dto;

import lombok.Data;

@Data
public class ActualizarEstadoCarritoRequest {
    private String nuevoEstado;
}