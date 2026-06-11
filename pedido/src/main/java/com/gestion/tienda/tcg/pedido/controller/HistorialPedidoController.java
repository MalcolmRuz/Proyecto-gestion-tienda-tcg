package com.gestion.tienda.tcg.pedido.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.tienda.tcg.pedido.dto.HistorialPedidoResponse;
import com.gestion.tienda.tcg.pedido.service.HistorialPedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/historial")
@RequiredArgsConstructor
@Tag(name = "HistorialPedidoController", description = "Controlador para gestionar el historial de los pedidos")
public class HistorialPedidoController {

    private final HistorialPedidoService historialService;

    @Operation(summary = "Obtener historial de un pedido", description = "Obtiene el historial de un pedido específico por su ID")
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<HistorialPedidoResponse>> obtenerHistorial(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(historialService.obtenerHistorial(pedidoId));
    }
}