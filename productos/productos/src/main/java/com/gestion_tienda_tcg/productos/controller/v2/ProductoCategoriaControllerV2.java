package com.gestion_tienda_tcg.productos.controller.v2;

import com.gestion_tienda_tcg.productos.assemblers.ProductoCategoriaModelAssembler;
import com.gestion_tienda_tcg.productos.dto.ProductoCategoriaRequest;
import com.gestion_tienda_tcg.productos.dto.ProductoCategoriaResponse;
import com.gestion_tienda_tcg.productos.service.ProductoCategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/productoCategoria")
@Tag(name = "PRODUCTO-CATEGORIA V2", description = "API HATEOAS para la gestión de relaciones Producto-Categoría utilizando EntityModel")
public class ProductoCategoriaControllerV2 {

    private final ProductoCategoriaService productoCategoriaService;
    private final ProductoCategoriaModelAssembler assembler;

    @Operation(summary = "Asignación de una categoría a un producto", description = "Crea una asignación y retorna el recurso con hipermedios")
    @PostMapping("/asignar")
    public ResponseEntity<EntityModel<ProductoCategoriaResponse>> asignarCategoria(@RequestBody ProductoCategoriaRequest request) {
        log.info("REST request V2 para asignar categoría {} al producto {}", request.getIdCategoria(), request.getIdProducto());
        var response = productoCategoriaService.asignarCategoriaAProducto(request);
        return new ResponseEntity<>(assembler.toModel(response), HttpStatus.CREATED);
    }

    @Operation(summary = "Elimina relación entre categoría y producto", description = "Desacopla la relación. No retorna cuerpo ni enlaces (204 No Content)")
    @DeleteMapping("/quitar/{idRelacion}")
    public ResponseEntity<Void> quitarCategoria(@PathVariable Long idRelacion) {
        log.info("REST request V2 para eliminar relación ID: {}", idRelacion);
        productoCategoriaService.quitarCategoriaDeProducto(idRelacion);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Búsqueda de todos los productos asociados a una categoría según ID")
    @GetMapping("/categoria/{idCategoria}")
    public CollectionModel<EntityModel<ProductoCategoriaResponse>> listarPorCategoria(@PathVariable Long idCategoria) {
        log.info("REST request V2 para listar productos de la categoría ID: {}", idCategoria);
        var lista = productoCategoriaService.listarProductosPorCategoria(idCategoria);
        return assembler.toCollectionModel(lista);
    }

    @Operation(summary = "Lista todas las relaciones producto-categorías")
    @GetMapping
    public CollectionModel<EntityModel<ProductoCategoriaResponse>> listarTodasLasRelaciones() {
        log.info("REST request V2 para listar todas las asociaciones");
        var lista = productoCategoriaService.listarCategorias();
        return assembler.toCollectionModel(lista);
    }
}