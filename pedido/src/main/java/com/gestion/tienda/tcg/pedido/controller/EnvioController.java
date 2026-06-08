package com.gestion.tienda.tcg.pedido.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.tienda.tcg.pedido.dto.EnvioResponse;
import com.gestion.tienda.tcg.pedido.enums.EstadoEnvio;
import com.gestion.tienda.tcg.pedido.service.EnvioService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/envios")
@RequiredArgsConstructor
public class EnvioController {

    private final EnvioService service;

    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<EnvioResponse> obtenerPorPedido(
            @PathVariable Long idPedido) {

        log.info("Obteniendo envío del pedido {}", idPedido);

        return ResponseEntity.ok(service.obtenerPorPedido(idPedido));
    }

    @PatchMapping("/pedido/{idPedido}")
    public ResponseEntity<EnvioResponse> actualizarEstado(
            @PathVariable Long idPedido,
            @RequestParam EstadoEnvio estado) {

        log.info("Actualizando estado del envío {}", idPedido);

        return ResponseEntity.ok(
                service.actualizarEstado(idPedido, estado));
    }
}