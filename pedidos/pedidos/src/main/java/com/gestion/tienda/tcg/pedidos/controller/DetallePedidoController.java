package com.gestion.tienda.tcg.pedidos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.tienda.tcg.pedidos.dto.DetallePedidoResponse;
import com.gestion.tienda.tcg.pedidos.service.DetallePedidoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/detalles")
@RequiredArgsConstructor
public class DetallePedidoController {

    private final DetallePedidoService service;

    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<List<DetallePedidoResponse>> listarPorPedido(
            @PathVariable Long idPedido) {

        log.info("Listando detalles del pedido {}", idPedido);

        return ResponseEntity.ok(service.listarPorPedido(idPedido));
    }
}