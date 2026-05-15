package com.gestion_tienda_tcg.productos.controller;

import com.gestion_tienda_tcg.productos.dto.ProductoRequest;
import com.gestion_tienda_tcg.productos.dto.ProductoResponse;
import com.gestion_tienda_tcg.productos.service.ProductoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/productos")
public class ProductoController {
    private final ProductoService productoService;
        @PostMapping
        public ResponseEntity<ProductoResponse> guardarProducto(@RequestBody ProductoRequest request) {
            log.info("REST request para guardar producto: {}", request.getNombre());
            var response = productoService.guardarProducto(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        @PutMapping("/{id}")
        public ResponseEntity<ProductoResponse> modificarProducto(
                @PathVariable Long id,
                @RequestBody ProductoRequest request) {
            log.info("REST request para modificar producto ID: {}", id);
            var response = productoService.modificarProducto(id, request);
            return ResponseEntity.ok(response);
        }

        @GetMapping("/{id}")
        public ResponseEntity<ProductoResponse> obtenerDetalle(@PathVariable Long id) {
            log.info("REST request para obtener detalle del producto ID: {}", id);
            var response = productoService.obtenerDetalle(id);
            return ResponseEntity.ok(response);
        }

        @PatchMapping("/{id}/desactivar")
        public ResponseEntity<Void> desactivarProducto(@PathVariable Long id) {
            log.info("REST request para desactivar producto ID: {}", id);
            productoService.desactivarProducto(id);
            return ResponseEntity.noContent().build();
        }

        @GetMapping("/proveedor/{idProveedor}")
        public ResponseEntity<List<ProductoResponse>> listarPorProveedor(@PathVariable Long idProveedor) {
            log.info("REST request para listar productos del proveedor ID: {}", idProveedor);
            var lista = productoService.listarPorProveedor(idProveedor);
            return ResponseEntity.ok(lista);
        }
    }




