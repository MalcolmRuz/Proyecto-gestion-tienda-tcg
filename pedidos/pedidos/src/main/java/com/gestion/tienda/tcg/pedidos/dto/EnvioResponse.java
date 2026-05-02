package com.gestion.tienda.tcg.pedidos.dto;

import java.time.LocalDateTime;

import com.gestion.tienda.tcg.pedidos.enums.EstadoPedido;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EnvioResponse {

    private Long idEnvio;

    private String direccionEnvio;

    private EstadoPedido estadoEnvio;

    private LocalDateTime fechaEnvio;
}
