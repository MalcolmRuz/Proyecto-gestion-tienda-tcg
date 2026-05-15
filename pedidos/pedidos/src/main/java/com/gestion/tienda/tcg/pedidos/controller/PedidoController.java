package com.gestion.tienda.tcg.pedidos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.tienda.tcg.pedidos.dto.CrearPedidoRequest;
import com.gestion.tienda.tcg.pedidos.dto.PedidoResponse;
import com.gestion.tienda.tcg.pedidos.enums.EstadoPedido;
import com.gestion.tienda.tcg.pedidos.service.PedidoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    // CREAR PEDIDO DESDE CARRITO
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

    // LISTAR PEDIDOS
    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listar() {

        log.info("Listando pedidos");

        return ResponseEntity.ok(pedidoService.listar());
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> obtenerPorId(
            @PathVariable Long id) {

        log.info("Buscando pedido {}", id);

        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    // ACTUALIZAR ESTADO
    @PatchMapping("/{id}/estado")
    public ResponseEntity<PedidoResponse> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoPedido estado) {

        log.info("Actualizando estado del pedido {}", id);

        return ResponseEntity.ok(pedidoService.actualizarEstado(id, estado));
    }
}