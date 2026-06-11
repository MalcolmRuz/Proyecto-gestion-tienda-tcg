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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
@Tag(name = "PedidoController", description = "Controlador para gestionar los pedidos")
public class PedidoController {

        private final PedidoService pedidoService;

        @Operation(summary = "Crear un nuevo pedido", description = "Crea un nuevo pedido a partir de un carrito, usuario y dirección de envío")
        @PostMapping
        public ResponseEntity<PedidoResponse> crear(@RequestBody CrearPedidoRequest request) {
                return ResponseEntity.ok(pedidoService.crearPedido(request.getIdCarrito(), request.getUsuarioId(),
                                request.getDireccionEnvio()));
        }

        @Operation(summary = "Listar todos los pedidos", description = "Obtiene una lista de todos los pedidos registrados en el sistema")
        @GetMapping
        public ResponseEntity<List<PedidoResponse>> listarTodos() {
                return ResponseEntity.ok(pedidoService.listarTodos());
        }

        @Operation(summary = "Buscar pedido por ID", description = "Obtiene los detalles de un pedido específico por su ID")
        @GetMapping("/{id}")
        public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
                return ResponseEntity.ok(pedidoService.buscarPorId(id));
        }

        @Operation
        @PutMapping("/{id}/{EstadoPedido}")
        public ResponseEntity<PedidoResponse> actualizarEstado(@PathVariable Long id,
                        @RequestParam EstadoPedido nuevoEstado) {
                return ResponseEntity.ok(pedidoService.actualizarEstadoPedido(id, nuevoEstado));
        }
}