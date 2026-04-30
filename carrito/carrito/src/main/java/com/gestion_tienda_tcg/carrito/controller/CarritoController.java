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

import com.gestion_tienda_tcg.carrito.enums.EstadoCarrito;
import com.gestion_tienda_tcg.carrito.model.Carrito;
import com.gestion_tienda_tcg.carrito.service.CarritoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/carritos")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    // CREAR
    @PostMapping
    public ResponseEntity<Carrito> crear(@RequestBody Carrito carrito) {

        log.info("Creando carrito");

        Carrito nuevo = carritoService.crear(carrito);

        return ResponseEntity.status(201).body(nuevo);
    }

    // LISTAR
    @GetMapping
    public ResponseEntity<List<Carrito>> listar() {

        log.info("Listando carritos");

        return ResponseEntity.ok(carritoService.listar());
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Carrito> obtener(@PathVariable Long id) {

        log.info("Buscando carrito {}", id);

        return ResponseEntity.ok(carritoService.buscarPorId(id));
    }

    // CAMBIAR ESTADO
    @PatchMapping("/{id}/estado/{estado}")
    public ResponseEntity<Carrito> cambiarEstado(@PathVariable Long id,
            @PathVariable EstadoCarrito estado) {

        log.info("Actualizando estado carrito {}", id);

        return ResponseEntity.ok(carritoService.actualizarEstado(id, estado));
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        log.warn("Eliminando carrito {}", id);

        carritoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
