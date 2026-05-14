package com.gestion_tienda_tcg.productos.controller;

import com.gestion_tienda_tcg.productos.dto.ProductoCategoriaRequest;
import com.gestion_tienda_tcg.productos.dto.ProductoCategoriaResponse;
import com.gestion_tienda_tcg.productos.service.ProductoCategoriaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/productoCategoria")
public class ProductoCategoriaController {
    private final ProductoCategoriaService productoCategoriaService;


        @PostMapping("/asignar")
        public ResponseEntity<ProductoCategoriaResponse> asignarCategoria(@RequestBody ProductoCategoriaRequest request) {
            log.info("REST request para asignar categoría {} al producto {}", request.getIdCategoria(), request.getIdProducto());
            var response = productoCategoriaService.asignarCategoriaAProducto(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        @DeleteMapping("/quitar/{idRelacion}")
        public ResponseEntity<Void> quitarCategoria(@PathVariable Long idRelacion) {
            log.info("REST request para eliminar relación ID: {}", idRelacion);
            productoCategoriaService.quitarCategoriaDeProducto(idRelacion);
            return ResponseEntity.noContent().build();
        }

        @GetMapping("/categoria/{idCategoria}")
        public ResponseEntity<List<ProductoCategoriaResponse>> listarPorCategoria(@PathVariable Long idCategoria) {
            log.info("REST request para listar productos de la categoría ID: {}", idCategoria);
            var lista = productoCategoriaService.listarProductosPorCategoria(idCategoria);
            return ResponseEntity.ok(lista);
        }

        @GetMapping
        public ResponseEntity<List<ProductoCategoriaResponse>> listarTodasLasRelaciones() {
            log.info("REST request para listar todas las asociaciones");
            var lista = productoCategoriaService.listarCategorias();
            return ResponseEntity.ok(lista);
        }
    }
