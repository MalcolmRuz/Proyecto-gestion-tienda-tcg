package com.gestion_tienda_tcg.productos.controller;

import com.gestion_tienda_tcg.productos.dto.ProveedorRequest;
import com.gestion_tienda_tcg.productos.dto.ProveedorResponse;
import com.gestion_tienda_tcg.productos.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/proveedor")
public class ProveedorController {
    private final ProveedorService proveedorService;
    @PostMapping
    public ResponseEntity<ProveedorResponse> registrarProveedor(@RequestBody ProveedorRequest request) {
        log.info("REST request para registrar proveedor: {}", request.getNombre());
        var response = proveedorService.registrarProveedor(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProveedorResponse>> listarProveedores() {
        log.info("REST request para listar todos los proveedores");
        var lista = proveedorService.listarProveedores();
        return ResponseEntity.ok(lista);
    }

    @PatchMapping("/{id}/contacto")
    public ResponseEntity<ProveedorResponse> actualizarContacto(
            @PathVariable Long id,
            @RequestParam String nuevoContacto) {
        log.info("REST request para actualizar contacto del proveedor ID: {}", id);
        var response = proveedorService.actualizarContacto(id, nuevoContacto);
        return ResponseEntity.ok(response);
    }
}
