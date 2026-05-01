package com.gestion_tienda_tcg.carrito.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gestion_tienda_tcg.carrito.dto.CarritoRequest;
import com.gestion_tienda_tcg.carrito.dto.CarritoResponse;
import com.gestion_tienda_tcg.carrito.enums.EstadoCarrito;
import com.gestion_tienda_tcg.carrito.service.CarritoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/carritos")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    // Crear
    @PostMapping
    public ResponseEntity<CarritoResponse> crearCarrito(@Valid @RequestBody CarritoRequest request) {

        log.info("Creando nuevo carrito");

        CarritoResponse response = carritoService.crear(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Listar
    @GetMapping
    public ResponseEntity<List<CarritoResponse>> listarCarritos() {

        log.info("Listando carritos");

        return ResponseEntity.ok(carritoService.listar());
    }

    // Buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<CarritoResponse> obtenerPorId(@PathVariable Long id) {

        log.info("Obteniendo carrito {}", id);

        return ResponseEntity.ok(carritoService.buscarPorId(id));
    }

    // Actualizar Estado
    @PatchMapping("/{id}/estado")
    public ResponseEntity<CarritoResponse> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoCarrito estado) {

        log.info("Actualizando estado carrito {}", id);

        return ResponseEntity.ok(carritoService.actualizarEstado(id, estado));
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCarrito(@PathVariable Long id) {

        log.warn("Eliminando carrito {}", id);

        carritoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
