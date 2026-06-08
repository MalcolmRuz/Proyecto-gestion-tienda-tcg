package com.gestion.tienda.tcg.pedido.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.tienda.tcg.pedido.dto.DetallePedidoResponse;
import com.gestion.tienda.tcg.pedido.service.DetallePedidoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/detalles")
@RequiredArgsConstructor
public class DetallePedidoController {

    private final DetallePedidoService detalleService;

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<DetallePedidoResponse>> listarPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(detalleService.listarPorPedido(pedidoId));
    }
}