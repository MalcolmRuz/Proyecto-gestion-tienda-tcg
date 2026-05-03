package com.gestion.tienda.tcg.carrito.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.tienda.tcg.carrito.dto.ActualizarEstadoRequest;
import com.gestion.tienda.tcg.carrito.dto.CarritoRequest;
import com.gestion.tienda.tcg.carrito.dto.CarritoResponse;
import com.gestion.tienda.tcg.carrito.service.CarritoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/carritos")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    // Crear carrito
    @PostMapping
    public ResponseEntity<CarritoResponse> crearCarrito(
            @Valid @RequestBody CarritoRequest request) {

        log.info("Creando nuevo carrito");

        CarritoResponse response = carritoService.crear(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Listar carritos
    @GetMapping
    public ResponseEntity<List<CarritoResponse>> listarCarritos() {

        log.info("Listando carritos");

        return ResponseEntity.ok(carritoService.listar());
    }

    // Buscar carrito por id
    @GetMapping("/{id}")
    public ResponseEntity<CarritoResponse> obtenerPorId(
            @PathVariable Long id) {

        log.info("Obteniendo carrito {}", id);

        return ResponseEntity.ok(carritoService.buscarPorId(id));
    }

    // Actualizar estado del carrito
    @PatchMapping("/{id}/estado")
    public ResponseEntity<CarritoResponse> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoRequest request) {

        log.info("Actualizando estado del carrito {}", id);

        return ResponseEntity.ok(
                carritoService.actualizarEstado(id, request.getEstado()));
    }

    // Eliminar carrito
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCarrito(
            @PathVariable Long id) {

        log.warn("Eliminando carrito {}", id);

        carritoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
