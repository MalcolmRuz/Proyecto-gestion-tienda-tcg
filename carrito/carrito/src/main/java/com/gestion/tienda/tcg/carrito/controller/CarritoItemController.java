package com.gestion.tienda.tcg.carrito.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.tienda.tcg.carrito.dto.ActualizarCantidadRequest;
import com.gestion.tienda.tcg.carrito.dto.CarritoItemRequest;
import com.gestion.tienda.tcg.carrito.dto.CarritoItemResponse;
import com.gestion.tienda.tcg.carrito.service.CarritoItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/carritos")
@RequiredArgsConstructor
public class CarritoItemController {

    private final CarritoItemService itemService;

    // =========================
    // Agregar item al carrito
    // =========================
    @PostMapping("/{idCarrito}/items")
    public ResponseEntity<CarritoItemResponse> agregarItem(
            @PathVariable @NonNull Long idCarrito,
            @Valid @RequestBody CarritoItemRequest request) {

        log.info("Agregando item al carrito {}", idCarrito);
        return ResponseEntity.ok(itemService.agregarItem(idCarrito, request));
    }

    // =========================
    // Listar items de un carrito
    // =========================
    @GetMapping("/{idCarrito}/items")
    public ResponseEntity<List<CarritoItemResponse>> listarItems(
            @PathVariable @NonNull Long idCarrito) {

        log.info("Listando items del carrito {}", idCarrito);
        return ResponseEntity.ok(itemService.listarPorCarrito(idCarrito));
    }

    // =========================
    // Buscar item específico dentro de un carrito
    // =========================
    @GetMapping("/{idCarrito}/items/{idItem}")
    public ResponseEntity<CarritoItemResponse> obtenerItemDeCarrito(
            @PathVariable @NonNull Long idCarrito,
            @PathVariable @NonNull Long idItem) {

        log.info("Buscando item {} dentro del carrito {}", idItem, idCarrito);
        return ResponseEntity.ok(itemService.buscarPorIdEnCarrito(idCarrito, idItem));
    }

    // =========================
    // Actualizar cantidad de un item
    // =========================
    @PatchMapping("/items/{idItem}")
    public ResponseEntity<CarritoItemResponse> actualizarCantidad(
            @PathVariable @NonNull Long idItem,
            @Valid @RequestBody ActualizarCantidadRequest request) {

        log.info("Actualizando cantidad del item {}", idItem);
        return ResponseEntity.ok(itemService.actualizarCantidad(idItem, request.getCantidad()));
    }

    // =========================
    // Eliminar item del carrito
    // =========================
    @DeleteMapping("/items/{idItem}")
    public ResponseEntity<Void> eliminarItem(@PathVariable @NonNull Long idItem) {
        log.warn("Eliminando item {}", idItem);
        itemService.eliminarItem(idItem);
        return ResponseEntity.noContent().build();
    }
}
