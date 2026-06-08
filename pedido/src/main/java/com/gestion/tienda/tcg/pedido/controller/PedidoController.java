package com.gestion.tienda.tcg.pedido.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.tienda.tcg.pedido.dto.CrearPedidoRequest;
import com.gestion.tienda.tcg.pedido.dto.PedidoResponse;
import com.gestion.tienda.tcg.pedido.enums.EstadoPedido;
import com.gestion.tienda.tcg.pedido.service.PedidoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
public class PedidoController {

        private final PedidoService pedidoService;

        @PostMapping
        public ResponseEntity<PedidoResponse> crear(@RequestBody CrearPedidoRequest request) {
                return ResponseEntity.ok(pedidoService.crearPedido(request.getIdCarrito(), request.getUsuarioId(),
                                request.getDireccionEnvio()));
        }

        @GetMapping
        public ResponseEntity<List<PedidoResponse>> listarTodos() {
                return ResponseEntity.ok(pedidoService.listarTodos());
        }

        @GetMapping("/{id}")
        public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
                return ResponseEntity.ok(pedidoService.buscarPorId(id));
        }

        @PutMapping("/{id}/estado")
        public ResponseEntity<PedidoResponse> actualizarEstado(@PathVariable Long id,
                        @RequestParam EstadoPedido nuevoEstado) {
                return ResponseEntity.ok(pedidoService.actualizarEstadoPedido(id, nuevoEstado));
        }
}