package com.gestion.tienda.tcg.pedido.controller.V1;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.tienda.tcg.pedido.dto.DetallePedidoResponse;
import com.gestion.tienda.tcg.pedido.service.DetallePedidoService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/detalles")
@RequiredArgsConstructor
@Tag(name = "DetallePedidoController", description = "Controlador para gestionar los detalles de los pedidos")
public class DetallePedidoController {

    private final DetallePedidoService detalleService;

    @Tag(name = "listarPorPedido", description = "Obtiene la lista de detalles de un pedido específico")
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<DetallePedidoResponse>> listarPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(detalleService.listarPorPedido(pedidoId));
    }
}