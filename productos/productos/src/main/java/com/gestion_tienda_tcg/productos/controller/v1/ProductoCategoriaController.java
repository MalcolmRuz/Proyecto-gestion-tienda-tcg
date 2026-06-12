package com.gestion_tienda_tcg.productos.controller.v1;

import com.gestion_tienda_tcg.productos.dto.ProductoCategoriaRequest;
import com.gestion_tienda_tcg.productos.dto.ProductoCategoriaResponse;
import com.gestion_tienda_tcg.productos.service.ProductoCategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "PRODUCTO-CATEGORIA", description = "Gestión de relaciones Producto-Categoría: Servicio para organizar productos dentro de categorías, permitiendo filtrado, asignación y administración de jerarquías de catálogo")
public class ProductoCategoriaController {
    private final ProductoCategoriaService productoCategoriaService;

        @Operation(summary = "Asignacion de una categoria a un producto",description = "ENDPOINT que a partir de un request con id de ambas tablas se crea una asignación")
        @PostMapping("/asignar")
        public ResponseEntity<ProductoCategoriaResponse> asignarCategoria(@RequestBody ProductoCategoriaRequest request) {
            log.info("REST request para asignar categoría {} al producto {}", request.getIdCategoria(), request.getIdProducto());
            var response = productoCategoriaService.asignarCategoriaAProducto(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        @Operation(summary = "Elimina relacion entre categoria y producto",description = "Endpoint encargado de desacoplar la relación entre un producto y una categoría")
        @DeleteMapping("/quitar/{idRelacion}")
        public ResponseEntity<Void> quitarCategoria(@PathVariable Long idRelacion) {
            log.info("REST request para eliminar relación ID: {}", idRelacion);
            productoCategoriaService.quitarCategoriaDeProducto(idRelacion);
            return ResponseEntity.noContent().build();
        }
        @Operation(summary = "Busqueda de todos los productos asociados a una categoría según ID",description = "A partir de una ID retorna una lista con todas las relaciones producto-categoria encontradas ")
        @GetMapping("/categoria/{idCategoria}")
        public ResponseEntity<List<ProductoCategoriaResponse>> listarPorCategoria(@PathVariable Long idCategoria) {
            log.info("REST request para listar productos de la categoría ID: {}", idCategoria);
            var lista = productoCategoriaService.listarProductosPorCategoria(idCategoria);
            return ResponseEntity.ok(lista);
        }
        @Operation(summary = "Lista todas las relaciones producto-categorias",description = "Retorna una lista con todas las relaciones producto-categoria creadas")
        @GetMapping
        public ResponseEntity<List<ProductoCategoriaResponse>> listarTodasLasRelaciones() {
            log.info("REST request para listar todas las asociaciones");
            var lista = productoCategoriaService.listarCategorias();
            return ResponseEntity.ok(lista);
        }
    }
