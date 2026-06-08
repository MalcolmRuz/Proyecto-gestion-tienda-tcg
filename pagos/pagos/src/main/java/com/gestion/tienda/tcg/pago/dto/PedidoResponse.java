package com.gestion.tienda.tcg.pago.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class PedidoResponse {

    private final Long idPedido;

    private final String estado;

    private final Double totalPedido;

}
