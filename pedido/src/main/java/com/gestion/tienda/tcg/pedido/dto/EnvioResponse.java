package com.gestion.tienda.tcg.pedido.dto;

import java.time.LocalDateTime;

import com.gestion.tienda.tcg.pedido.enums.EstadoEnvio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnvioResponse {

    private Long idEnvio;

    private String direccionEnvio;

    private EstadoEnvio estadoEnvio;

    private LocalDateTime fechaEnvio;
}