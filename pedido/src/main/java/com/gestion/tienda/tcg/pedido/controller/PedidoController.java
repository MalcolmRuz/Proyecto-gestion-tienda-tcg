package com.gestion.tienda.tcg.pedido.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.tienda.tcg.pedido.dto.CrearPedidoRequest;
import com.gestion.tienda.tcg.pedido.dto.PedidoResponse;
import com.gestion.tienda.tcg.pedido.enums.EstadoPedido;
import com.gestion.tienda.tcg.pedido.service.PedidoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
public class PedidoController {

        private final PedidoService pedidoService;

        // ================================
        // CREAR PEDIDO DESDE CARRITO
        // ================================
        @PostMapping("/{idCarrito}")
        public ResponseEntity<PedidoResponse> crearPedido(
                        @PathVariable Long idCarrito,
                        @Valid @RequestBody CrearPedidoRequest request) {

                log.info("Creando pedido desde carrito {}", idCarrito);

                PedidoResponse response = pedidoService.crearPedido(
                                idCarrito,
                                request.getUsuarioId(),
                                request.getEnvio());

                return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        // ================================
        // LISTAR TODOS
        // ================================
        @GetMapping
        public ResponseEntity<List<PedidoResponse>> listar() {

                log.info("Listando pedidos");

                return ResponseEntity.ok(
                                pedidoService.listar());
        }

        // ================================
        // OBTENER POR ID
        // ================================
        @GetMapping("/{id}")
        public ResponseEntity<PedidoResponse> obtenerPorId(
                        @PathVariable @NonNull Long id) {

                log.info("Buscando pedido {}", id);

                return ResponseEntity.ok(
                                pedidoService.buscarPorId(id));
        }

        // ================================
        // LISTAR POR USUARIO
        // ================================
        @GetMapping("/usuario/{usuarioId}")
        public ResponseEntity<List<PedidoResponse>> listarPorUsuario(
                        @PathVariable Long usuarioId) {

                log.info("Listando pedidos del usuario {}", usuarioId);

                return ResponseEntity.ok(
                                pedidoService.listarPorUsuario(usuarioId));
        }

        // ================================
        // ACTUALIZAR ESTADO
        // ================================
        @PatchMapping("/{id}/estado")
        public ResponseEntity<PedidoResponse> actualizarEstado(
                        @PathVariable @NonNull Long id,
                        @RequestParam EstadoPedido estado) {

                log.info("Actualizando estado del pedido {}", id);

                return ResponseEntity.ok(
                                pedidoService.actualizarEstado(id, estado));
        }

        // ================================
        // MARCAR ENTREGADO
        // ================================
        @PatchMapping("/{id}/entregado")
        public ResponseEntity<PedidoResponse> marcarEntregado(
                        @PathVariable Long id) {

                log.info("Marcando pedido {} como ENTREGADO", id);

                return ResponseEntity.ok(
                                pedidoService.marcarEntregado(id));
        }
}