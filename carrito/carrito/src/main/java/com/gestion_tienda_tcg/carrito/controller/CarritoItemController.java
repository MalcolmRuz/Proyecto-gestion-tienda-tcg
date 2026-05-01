package com.gestion_tienda_tcg.carrito.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion_tienda_tcg.carrito.dto.CarritoItemRequest;
import com.gestion_tienda_tcg.carrito.dto.CarritoItemResponse;
import com.gestion_tienda_tcg.carrito.service.CarritoItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/carritos/items")
@RequiredArgsConstructor
public class CarritoItemController {
    private final CarritoItemService itemService;

    // Agregar item al carrito
    @PostMapping("/{idCarrito}")
    public ResponseEntity<CarritoItemResponse> agregarItem(
            @PathVariable Long idCarrito,
            @Valid @RequestBody CarritoItemRequest request) {

        log.info("Agregando item al carrito {}", idCarrito);

        return ResponseEntity.ok(itemService.agregarItem(idCarrito, request));
    }

    // Listar items del carrito
    @GetMapping("/carrito/{idCarrito}")
    public ResponseEntity<List<CarritoItemResponse>> listarItems(
            @PathVariable Long idCarrito) {

        log.info("Listando items del carrito {}", idCarrito);

        return ResponseEntity.ok(itemService.listarPorCarrito(idCarrito));
    }

    // Buscar item por Id
    @GetMapping("/{idItem}")
    public ResponseEntity<CarritoItemResponse> obtenerItem(
            @PathVariable Long idItem) {

        log.info("Obteniendo item {}", idItem);

        return ResponseEntity.ok(itemService.buscarPorId(idItem));
    }

    // Actualizar cantidad de item
    @PatchMapping("/{idItem}/cantidad/{cantidad}")
    public ResponseEntity<CarritoItemResponse> actualizarCantidad(
            @PathVariable Long idItem,
            @PathVariable Integer cantidad) {

        log.info("Actualizando cantidad del item {}", idItem);

        return ResponseEntity.ok(itemService.actualizarCantidad(idItem, cantidad));
    }

    // Eliminar item
    @DeleteMapping("/{idItem}")
    public ResponseEntity<Void> eliminarItem(@PathVariable Long idItem) {

        log.warn("Eliminando item {}", idItem);

        itemService.eliminarItem(idItem);

        return ResponseEntity.noContent().build();
    }
}
