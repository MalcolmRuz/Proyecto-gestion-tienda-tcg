package com.gestion.tienda.tcg.pedido.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.tienda.tcg.pedido.dto.HistorialPedidoResponse;
import com.gestion.tienda.tcg.pedido.service.HistorialPedidoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/historial")
@RequiredArgsConstructor
public class HistorialPedidoController {

    private final HistorialPedidoService historialService;

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<HistorialPedidoResponse>> obtenerHistorial(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(historialService.obtenerHistorial(pedidoId));
    }
}